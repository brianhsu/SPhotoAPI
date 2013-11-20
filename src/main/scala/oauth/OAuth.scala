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
   *  @param    method      HTTP method type.
   *  @param    params      Parameters to send.
   */
  def buildRequest(url: String, method: Verb, 
                   params: (String, String)*): OAuthRequest = 
  {

    val fullURL = if(url.startsWith("http")) url else prefixURL + url
    val request = new OAuthRequest(method, fullURL)

    if (method == Verb.POST) {
      params.foreach { case(key, value) => request.addBodyParameter(key, value) }
    } else if (method == Verb.GET) {
      params.foreach { case(key, value) => request.addQuerystringParameter(key, value) }
    }

    request
  }

  /**
   *  Send Request and Get Response from Server
   *  
   *  @param    url       The API endpoint URL
   *  @param    verb      The HTTP request method
   *  @param    params    The parameters to API method
   *  @return             Try[(responseCode, contentType, responseBody)]
   */
  def sendRequest_(url: String, verb: Verb, params: (String, String)*): Try[(Int, String, String)] = 
  {
    Try {

      if (System.currentTimeMillis > expireAt.getTime) {
        refreshAccessToken()
      }

      val request = buildRequest(url, verb, params: _*)
      service.signRequest(accessToken getOrElse null, request)
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

  /**
   *  Get authorization URL
   *
   *  @return   The ImgUr authorization page if success.
   */
  def getAuthorizationURL: Try[String] = Try(service.getAuthorizationUrl(null))

  /**
   *  Authorize ImgUr
   *
   *  @param  verifyCode  The pin / code returned by ImgUr
   *  @return             Success[Unit] if success.
   */
  def authorize(verifyCode: String): Try[Unit] = Try {
    
    val currentTime = System.currentTimeMillis
    val verifier = new Verifier(verifyCode)
    val accessToken = service.getAccessToken(null, verifier)
    val jsonResponse = JsonParser.parse(accessToken.getRawResponse)

    val (rawAccessToken, refreshToken, expiresAt) = OAuth.parseTokenJSON(jsonResponse)

    this.accessToken = Some(accessToken)
    this.refreshToken = refreshToken
    this.expireAt = new Date(currentTime + expiresAt * 1000)
  }

  /**
   *  Update access token from refreshToken
   */
  protected def refreshAccessToken() 
  {

    if (refreshToken.isDefined) {

      val request = buildRequest(
        refreshURL, Verb.POST,
        "refresh_token" -> refreshToken.get,
        "client_id" -> appKey,
        "client_secret" -> appSecret,
        "grant_type" -> "refresh_token"
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

