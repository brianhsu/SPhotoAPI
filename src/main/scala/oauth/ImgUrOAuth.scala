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
class ImgUrOAuth(override val appKey: String, override val appSecret: String,
                 override protected[sphotoapi] val service: OAuthService,
                 override protected[sphotoapi] var accessToken: Option[Token] = None, 
                 override protected[sphotoapi] var refreshToken: Option[String] = None,
                 override protected[sphotoapi] var expireAt: Date = new Date) extends OAuth
{

  protected val prefixURL = "https://api.imgur.com/"
  protected val refreshURL = "oauth2/token"


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

    def parseResponse(contentType: String, body: String) = Try {
      contentType match {
        case "text/xml" => Left(parseXML(body))
        case "application/json" => Right(parseJSON(body))
      }
    }

    for {
      (code, contentType, body) <- sendRequest_(url, verb, params:_*)
      response <- parseResponse(contentType, body)
    } yield response

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


