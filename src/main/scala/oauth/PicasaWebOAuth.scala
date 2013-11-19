package org.bone.sphotoapi.oauth

import org.scribe.oauth.OAuthService
import org.scribe.model.Token
import org.scribe.model.Verb

import scala.util.Try
import scala.xml.Node
import scala.xml.XML

/**
 *  Used for Unit-test only.
 */
private[sphotoapi] trait MockPicasaWebOAuth extends PicasaWebOAuth

class PicasaWebOAuth(appKey: String, appSecret: String,
                 private[sphotoapi] val service: OAuthService,
                 private[sphotoapi] var accessToken: Option[Token]) extends OAuth
{

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

