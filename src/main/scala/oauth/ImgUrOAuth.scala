package org.bone.sphotoapi.oauth

import org.scribe.oauth.OAuthService
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.model.OAuthRequest

import java.util.Date

import net.liftweb.json.JsonParser
import net.liftweb.json.JsonAST._

import scala.util.Try
import scala.xml.Node
import scala.xml.XML


/**
 *  ImgUr OAuth Helper object
 *
 *  @param  imgUrAPIPrefix    ImgUr API endpoint prefix
 *  @param  appKey            OAuth Cleint App Key
 *  @param  appSecret         OAuth Client App Secret
 *  @param  service           Scribe OAuth service
 *  @param  accessToken       The access token from ImgUr API
 *  @param  refreshToken      The refresh token from ImgUr API
 *  @param  expireAt          When will access token will be expired.
 */
class ImgUrOAuth(appKey: String, appSecret: String,
                 private[sphotoapi] val service: OAuthService,
                 private[sphotoapi] var accessToken: Option[Token] = None, 
                 private[sphotoapi] var refreshToken: Option[String] = None,
                 private[sphotoapi] var expireAt: Date = new Date) extends OAuth
{

  protected val prefixURL = "https://api.imgur.com/"

  /**
   *  Send Request and Parse Response to JSON / XML
   *  
   *  @param    url       The API endpoint URL
   *  @param    verb      The HTTP request method
   *  @param    params    The parameters to API method
   *  @return             The XML node or JSON object if successfully called API.
   */
  def sendRequest(url: String, verb: Verb, 
                  params: (String, String)*): Try[Either[Node, JValue]] = 
  {
    
    Try {

      if (System.currentTimeMillis > expireAt.getTime) {
        refreshAccessToken()
      }

      val request = buildRequest(url, verb, params: _*)
      
      service.signRequest(accessToken getOrElse null, request)

      val response = request.send

      response.getHeader("Content-Type") match {
        case "text/xml" => Left(parseXML(response.getBody))
        case "application/json" => Right(parseJSON(response.getBody))
      }

    }

  }

  /**
   *  Update access token from refreshToken
   */
  private def refreshAccessToken() 
  {

    if (refreshToken.isDefined) {

      val request = buildRequest(
        "oauth2/token", Verb.POST,
        "refresh_token" -> refreshToken.get,
        "client_id" -> appKey,
        "client_secret" -> appSecret,
        "grant_type" -> "refresh_token"
      )

      val currentTime = System.currentTimeMillis

      val rawResponse = request.send.getBody
      val jsonResponse = JsonParser.parse(rawResponse)

      val (newAccessToken, newRefreshToken, expiresInSecond) = 
        ImgUrOAuth.parseTokenJSON(jsonResponse)
      
      this.accessToken = Some(new Token(newAccessToken, "", rawResponse))
      this.refreshToken = Some(newRefreshToken)
      this.expireAt = new Date(currentTime + expiresInSecond.toLong * 1000)
    }

  }


  /**
   *  Parse response content to XML node
   *
   *  @param    rawContent  
   *  @return   XML node corresponding to rawContent
   */
  private def parseXML(rawContent: String): Node = 
  {
    val xmlNode = XML.loadString(rawContent)
    val errorMessage = xmlNode \\ "error"

    if (!errorMessage.isEmpty) {
      throw new OAuthException(errorMessage.text, rawContent)
    }

    xmlNode
  }

  /**
   *  Parse response content to JSON object.
   *
   *  @param    rawContent  
   *  @return   JSON corresponding to rawContent
   */
  private def parseJSON(rawContent: String): JValue = 
  {
    val json = JsonParser.parse(rawContent)
    val errorMessage = (json \\ "error" \ "message").toOpt

    if (errorMessage.isDefined) {
      throw new OAuthException(errorMessage.get.toString, rawContent)
    }

    json
  }

}

object ImgUrOAuth {

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

