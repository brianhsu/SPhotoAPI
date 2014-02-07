package org.bone.sphotoapi.oauth

import org.scribe.model.Verb
import org.scribe.model.OAuthRequest
import org.scribe.oauth.OAuthService

import org.scribe.model.Token
import org.scribe.model.Verifier

import scala.util.Try

import net.liftweb.json.JsonParser
import net.liftweb.json.JsonAST._

import java.util.Date
import java.net.URLEncoder

/**
 *  Used for Unit-test only.
 */
private[sphotoapi] trait MockOAuth extends OAuth

/**
 *  Tools for build HTTP request to OAuth service.
 */
abstract class OAuth {

  def appKey: String
  def appSecret: String

  private var requestToken: Option[Token] = None

  protected def prefixURL: String
  protected def refreshURL: String

  protected[sphotoapi] var accessToken: Option[Token]
  protected[sphotoapi] var refreshToken: Option[String]
  protected[sphotoapi] var expireAt: Date
  protected[sphotoapi] val service: OAuthService


  /**
   *  Create OAuthReqeust object and attatch params to it.
   *
   *  @param    url         API Endpoint.
   *  @param    verb        HTTP method type.
   *  @param    params      Parameters to send.
   */
  def buildRequest(url: String, 
                   verb: Verb, 
                   getParams: Map[String, String] = Map.empty,
                   postParams: Map[String, String] = Map.empty): OAuthRequest = 
  {

    val fullURL = if(url.startsWith("http")) url else prefixURL + url
    val request = new OAuthRequest(verb, fullURL)

    getParams.foreach { case(key, value) => request.addQuerystringParameter(key, value) }
    postParams.foreach { case(key, value) => request.addBodyParameter(key, value) }

    request
  }

  /**
   *  Send Request and Get Response from Server
   *  
   *  @param    url       The API endpoint URL
   *  @param    verb      The HTTP request method
   *  @param    payload   The payload sends with HTTP
   *  @param    params    The parameters to API method
   *  @return             Try[(responseCode, contentType, responseBody)]
   */
  def sendRequest_(url: String, verb: Verb, 
                   queryParams: Map[String, String] = Map.empty,
                   postParams: Map[String, String] = Map.empty,
                   payload: Option[Array[Byte]] = None): Try[(Int, String, String)] = 
  {
    Try {

      if (service.getVersion == "2.0" && System.currentTimeMillis > expireAt.getTime) {
        refreshAccessToken()
      }

      val request = buildRequest(url, verb, queryParams, postParams)
      service.signRequest(accessToken getOrElse null, request)
      payload.foreach(request.addPayload)
      val response = request.send

      (response.getCode, response.getHeader("Content-Type"), response.getBody)
    }
  }

  /**
   *  Get current refresh token of ImgUr API
   *
   *  You could use refreshToken to verify ImgUr authorization,
   *  instead of direct user to ImgUr authorization page again.
   *
   *  @return   Current refresh token
   */
  def getRefreshToken = refreshToken

  def getAuthorizationURLv1(params: (String, String)*) = {

    val urlParameter = params.map { case(name, value) => 
      val encodedName = URLEncoder.encode(name, "utf-8")
      val encodedValue = URLEncoder.encode(value, "utf-8")
      s"$encodedName=$encodedValue" 
    }.mkString("&", "&", "")

    this.requestToken = Some(service.getRequestToken)

    params match {
      case Nil => service.getAuthorizationUrl(requestToken.get)
      case _   => service.getAuthorizationUrl(requestToken.get) + urlParameter
    }

  }

  def getAuthorizationURLv2(params: (String, String)*) = {
    val urlParameter = params.map { case(name, value) => 
      val encodedName = URLEncoder.encode(name, "utf-8")
      val encodedValue = URLEncoder.encode(value, "utf-8")
      s"$encodedName=$encodedValue" 
    }.mkString("&", "&", "")

    params match {
      case Nil => service.getAuthorizationUrl(null)
      case _   => service.getAuthorizationUrl(null) + urlParameter
    }
  }

  /**
   *  Get authorization URL
   *
   *  @return   The ImgUr authorization page if success.
   */
  def getAuthorizationURL(params: (String, String)*): Try[String] = Try {
    if (service.getVersion == "2.0") {
      getAuthorizationURLv2(params: _*)
    } else {
      getAuthorizationURLv1(params: _*)
    }
  }

  /**
   *  Authorize ImgUr
   *
   *  @param  verifyCode  The pin / code returned by ImgUr
   *  @return             Success[Unit] if success.
   */
  def authorize(verifyCode: String): Try[Unit] = Try {

    val verifier = new Verifier(verifyCode)

    requestToken match {
      case Some(token) =>
        val accessToken = service.getAccessToken(token, verifier)
        this.accessToken = Some(accessToken)

      case None => {
        val currentTime = System.currentTimeMillis
        val accessToken = service.getAccessToken(null, verifier)
        val jsonResponse = JsonParser.parse(accessToken.getRawResponse)

        val (rawAccessToken, refreshToken, expiresAt) = OAuth.parseTokenJSON(jsonResponse)

        this.accessToken = Some(accessToken)
        this.refreshToken = refreshToken
        this.expireAt = new Date(currentTime + expiresAt * 1000)
      }
    }

  }

  /**
   *  Update access token from refreshToken
   */
  protected def refreshAccessToken() 
  {

    if (refreshToken.isDefined) {

      val request = buildRequest(
        url = refreshURL, 
        verb = Verb.POST, 
        postParams = Map(
          "refresh_token" -> refreshToken.get,
          "client_id" -> appKey,
          "client_secret" -> appSecret,
          "grant_type" -> "refresh_token"
        )
      )

      val currentTime = System.currentTimeMillis

      val rawResponse = request.send.getBody
      println("rawResponse:" + rawResponse)

      val jsonResponse = JsonParser.parse(rawResponse)
      val (newAccessToken, newRefreshToken, expiresInSecond) = 
        OAuth.parseTokenJSON(jsonResponse)
      
      this.accessToken = Some(new Token(newAccessToken, "", rawResponse))
      this.refreshToken = newRefreshToken
      this.expireAt = new Date(currentTime + expiresInSecond.toLong * 1000)
    }
  }

}

object OAuth {

  /**
   *  Parse token returned by ImgUr API.
   *
   *  @param  json    The JSON represent of ImgUr token response
   *  @return         (accessToken, refreshToken, expiresInSecond)
   */
  def parseTokenJSON(json: JValue): (String, Option[String], Int)  = {
    val JString(accessToken)  = json \ "access_token"
    val JInt(expiresInSecond) = json \ "expires_in"
    val refreshToken = (json \ "refresh_token").toOpt.map(_.values.toString)

    (accessToken, refreshToken, expiresInSecond.toInt)
  }

}

