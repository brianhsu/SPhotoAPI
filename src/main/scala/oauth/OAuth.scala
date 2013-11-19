package org.bone.sphotoapi.oauth

import org.scribe.model.Verb
import org.scribe.model.OAuthRequest
import org.scribe.model.Token

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
      val jsonResponse = JsonParser.parse(rawResponse)

      val (newAccessToken, newRefreshToken, expiresInSecond) = 
        OAuth.parseTokenJSON(jsonResponse)
      
      this.accessToken = Some(new Token(newAccessToken, "", rawResponse))
      this.refreshToken = Some(newRefreshToken)
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
  def parseTokenJSON(json: JValue): (String, String, Int)  = {
    val JString(accessToken)  = json \ "access_token"
    val JString(refreshToken) = json \ "refresh_token"
    val JInt(expiresInSecond) = json \ "expires_in"

    (accessToken, refreshToken, expiresInSecond.toInt)
  }

}

