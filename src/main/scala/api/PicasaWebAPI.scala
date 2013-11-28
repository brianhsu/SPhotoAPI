package org.bone.sphotoapi.api

import org.bone.sphotoapi.oauth._
import org.bone.sphotoapi.parser._
import org.bone.sphotoapi.model._
import org.bone.sphotoapi.scribe._

import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.Google2Api
import org.scribe.model.Verb

import scala.util.Try
import java.util.Date
import net.liftweb.json.JsonParser
import net.liftweb.json.JsonAST._

/**
 *  PicasaWeb API
 *
 *  Use this class to access PicasaWeb API, you should create it
 *  by calling method in PicasaWebAPI companion object.
 *  
 *  @param  picasaWebOAuth  PicasaWebOAuth access object
 */
class PicasaWebAPI private(override val oauth: PicasaWebOAuth) extends API(oauth)
{

  /**
   *  Get Photos From Album
   *
   *  @param    albumID     The ID of album
   *  @param    userID      The ID of album's owner
   *  @return               Success[List[Photo]] if everything is fine.
   */
  def getPhotos(albumID: String, userID: String = "default", imageMaxSize: String = "640u"): Try[List[Photo]] = {

    val endPoint = s"user/$userID/albumid/$albumID?imgmax=$imageMaxSize"

    oauth.sendRequest(endPoint, Verb.GET).map { response =>
      PicasaWebPhoto.fromXML(response)
    }

  }

  /**
   *  Get User's Album list
   *
   *  @param    userID   userID of album onwer, "default" if you want to fetch current user's alubm.
   *  @return            Success[List[Album]] if everything is fine.
   */
  def getAlbums(userID: String = "default"): Try[List[Album]] = {

    val endPoint = s"user/$userID"

    oauth.sendRequest(endPoint, Verb.GET).map { response =>
      PicasaWebAlbum.fromXML(response)
    }
  }

  override def getPhotos(albumID: String): Try[List[Photo]] = getPhotos(albumID, "default", "d")
  override def getAlbums(): Try[List[Album]] = getAlbums("default")

  /**
   *  Get user information
   *
   *  @return   Try[(google UserID, EMail)]
   */
  override def getUserInfo: Try[(String, String)] = {

    def parseResponse(body: String) = Try {
      val jsonResponse = JsonParser.parse(body)
      val JString(userID) = jsonResponse \ "id"
      val JString(email) = jsonResponse \ "email"

      (userID, email)
    }

    for {
      (code, contentType, body) <- oauth.sendRequest_("https://www.googleapis.com/oauth2/v1/userinfo", Verb.GET)
      (userID, email) <- parseResponse(body)
    } yield (userID, email)
  }

}

/**
 *  PicasaWeb API
 *
 */
object PicasaWebAPI {

  /**
   *  Create PicasaWeb API that verify by user enter PIN directly.
   *
   *  @param    appKey            The app key you got from PicasaWeb.
   *  @param    appSecret         The app secret you got from PicasaWeb.
   *  @return                     PicasaWebAPI object
   */
  def apply(appKey: String, appSecret: String): PicasaWebAPI = 
  {

    val service = (new ServiceBuilder).
                    provider(classOf[Google2Api]).
                    apiKey(appKey).
                    apiSecret(appSecret).
                    callback("urn:ietf:wg:oauth:2.0:oob").
                    scope("email https://picasaweb.google.com/data/").build

    val oauth = new PicasaWebOAuth(appKey, appSecret, service, accessToken = None)

    new PicasaWebAPI(oauth)
  }

  /**
   *  Create PicasaWeb API that has callback.
   *
   *  @param    appKey            The app key you got from PicasaWeb.
   *  @param    appSecret         The app secret you got from PicasaWeb.
   *  @param    callback          The callback URL
   *  @return                     PicasaWebAPI object
   */
  def withCallback(appKey: String, appSecret: String, callback: String): PicasaWebAPI = 
  {

    val service = (new ServiceBuilder).
                    provider(classOf[Google2Api]).
                    apiKey(appKey).
                    apiSecret(appSecret).
                    scope("email https://picasaweb.google.com/data/").
                    callback(callback).build

    val oauth = new PicasaWebOAuth(appKey, appSecret, service, accessToken = None)

    new PicasaWebAPI(oauth)
  }

  /**
   *  Create PicasaWeb API using refreshToken
   *
   *  You could create PicasaWeb API by provide refreshToken.
   *
   *  In this case, the PicasaWeb will be alredy verified when it's created,
   *  which means you don't need go through the whole authroization process.
   *  
   *
   *  @param    appKey            The app key you got from ImgUr.
   *  @param    appSecret         The app secret you got from ImgUr.
   *  @param    refreshToken      The refresh token you got from ImgUr.
   *  @return                     ImgUrAPI object
   */
  def withRefreshToken(appKey: String, appSecret: String, 
                       refreshToken: String) = 
  {
    val service = (new ServiceBuilder).
                    provider(classOf[Google2Api]).
                    apiKey(appKey).
                    apiSecret(appSecret).build

    val oauth = new PicasaWebOAuth(
      appKey, appSecret, service,
      accessToken = None,
      refreshToken = Some(refreshToken),
      expireAt = new Date
    )

    new PicasaWebAPI(oauth)
   
  }

  /**
   *  Used for Unit-test only.
   */
  private[api] def withMock(mockOAuth: PicasaWebOAuth with MockOAuth) = new PicasaWebAPI(mockOAuth)
}
