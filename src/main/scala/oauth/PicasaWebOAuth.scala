package org.bone.sphotoapi.oauth

import org.scribe.oauth.OAuthService
import org.scribe.model.Token
import org.scribe.model.Verb

import java.util.Date

import scala.util.Try
import scala.xml.Node
import scala.xml.XML

class PicasaWebOAuth(override val appKey: String, override val appSecret: String,
                 private[sphotoapi] val service: OAuthService,
                 override protected[sphotoapi] var accessToken: Option[Token] = None, 
                 override protected[sphotoapi] var refreshToken: Option[String] = None,
                 override protected[sphotoapi] var expireAt: Date = new Date) extends OAuth
{

  protected val refreshURL = "/o/oauth2/token"
  protected val prefixURL = "https://picasaweb.google.com/data/feed/api/"

  /**
   *  Send Request and Parse Response to JSON / XML
   *  
   *  @param    url       The API endpoint URL
   *  @param    verb      The HTTP request method
   *  @param    params    The parameters to API method
   *  @return             The XML node or JSON object if successfully called API.
   */
  def sendRequest(url: String, verb: Verb, 
                  params: (String, String)*): Try[Node] = 
  {
    
    Try {

      if (System.currentTimeMillis > expireAt.getTime) {
        refreshAccessToken()
      }

      val request = buildRequest(url, verb, params: _*)
      
      service.signRequest(accessToken getOrElse null, request)

      val response = request.send
      val contentType = response.getHeader("Content-Type")
      val responseContent = response.getBody

      try {
        XML.loadString(responseContent)
      } catch {
        case e: Exception => throw new Exception(responseContent)
      }
    }
  }
}

