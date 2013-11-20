package org.bone.sphotoapi.api

import org.bone.sphotoapi.oauth._
import org.bone.sphotoapi.parser._
import org.bone.sphotoapi.model._
import org.bone.sphotoapi.scribe._

import net.liftweb.json.JsonAST._

import org.scribe.builder.ServiceBuilder
import org.scribe.model.Verb

import scala.util.Try
import java.util.Date

/**
 *  ImgUr API
 *
 *  Use this class to access ImgUr API, you should create it
 *  by calling method in ImgUrAPI companion object.
 *  
 *  @param  oauth  ImgUrOAuth access object
 */
class ImgUrAPI private(override val oauth: ImgUrOAuth) extends API(oauth)
{

  /**
   *  Get Photos From Album
   *
   *  @param    albumID     The ID of album
   *  @return               Success[List[Photo]] if everything is fine.
   */
  override def getPhotos(albumID: String): Try[List[Photo]] = {

    val endPoint = s"3/album/$albumID/images.xml"

    oauth.sendRequest(endPoint, Verb.GET).map { response =>
      ImgUrPhoto.fromXML(response.left.get)
    }

  }

  /**
   *  Get User's Album list
   *
   *  @return               Success[List[Album]] if everything is fine.
   */
  override def getAlbums(): Try[List[Album]] = getAlbums("me", None, None)

  override def getUserInfo(): Try[(String, String)] = {
    
    def parseUserID(json: JValue) = (json \\ "id").values.toString
    def parseEMail(json: JValue) = (json \\ "email").values.toString

    val userIDResponse = oauth.sendRequest("3/account/me", Verb.GET)
    val emailResponse = oauth.sendRequest("3/account/me/settings", Verb.GET)

    for {
      userID <- userIDResponse.map(x => parseUserID(x.right.get))
      email <- emailResponse.map(x => parseEMail(x.right.get))
    } yield (userID, email)
  }

  /**
   *  Get User's Album list
   *
   *  @param    username    username, "me" if you want to fetch current user's alubm.
   *  @return               Success[List[Album]] if everything is fine.
   */
  def getAlbums(username: String, 
                page: Option[Int] = None, 
                perPage: Option[Int] = None): Try[List[Album]] = {

    val endPoint = s"3/account/$username/albums.xml"
    val params = (
      page.map("page" -> _.toString) ++ 
      perPage.map("perPage" -> _.toString)
    ).toList

    oauth.sendRequest(endPoint, Verb.GET, params: _*).map { response =>
      ImgUrAlbum.fromXML(response.left.get)
    }
    
  }

}

/**
 *  ImgUr API
 *
 */
object ImgUrAPI {

  /**
   *  Create ImgUr API that verify by user enter PIN directly.
   *
   *  @param    appKey            The app key you got from ImgUr.
   *  @param    appSecret         The app secret you got from ImgUr.
   *  @return                     ImgUrAPI object
   */
  def apply(appKey: String, appSecret: String): ImgUrAPI = 
  {

    val service = (new ServiceBuilder).
                    provider(classOf[ImgUr3Api]).
                    apiKey(appKey).
                    apiSecret(appSecret).build

    val oauth = new ImgUrOAuth(
      appKey, appSecret, service,
      accessToken = None,
      refreshToken = None,
      expireAt = new Date
    )

    new ImgUrAPI(oauth)
  }

  /**
   *  Create ImgUr API that has callback.
   *
   *  @param    appKey            The app key you got from ImgUr.
   *  @param    appSecret         The app secret you got from ImgUr.
   *  @param    callback          The callback URL
   *  @return                     ImgUrAPI object
   */
  def withCallback(appKey: String, appSecret: String, callback: String): ImgUrAPI = 
  {

    val service = (new ServiceBuilder).
                    provider(classOf[ImgUr3Api]).
                    apiKey(appKey).
                    apiSecret(appSecret).
                    callback(callback).build

    val oauth = new ImgUrOAuth(
      appKey, appSecret, service,
      accessToken = None,
      refreshToken = None,
      expireAt = new Date
    )

    new ImgUrAPI(oauth)

  }

  /**
   *  Create ImgUr API using refreshToken
   *
   *  You could create ImgUr API by provide refreshToken.
   *
   *  In this case, the ImgUrAPI will be alredy verified when it's created,
   *  which means you don't need go through the whole authroization process.
   *  
   *
   *  @param    appKey            The app key you got from ImgUr.
   *  @param    appSecret         The app secret you got from ImgUr.
   *  @param    refreshToken      The refresh token you got from ImgUr.
   *  @return                     ImgUrAPI object
   */
  def withRefreshToken(appKey: String, appSecret: String, 
                       refreshToken: String): ImgUrAPI = 
  {
    val service = (new ServiceBuilder).
                    provider(classOf[ImgUr3Api]).
                    apiKey(appKey).
                    apiSecret(appSecret).build

    val oauth = new ImgUrOAuth(
      appKey, appSecret, service,
      accessToken = None,
      refreshToken = Some(refreshToken),
      expireAt = new Date
    )

    new ImgUrAPI(oauth)
   
  }

  /**
   *  Used for Unit-test only.
   */
  private[api] def withMock(mockOAuth: ImgUrOAuth with MockOAuth) = new ImgUrAPI(mockOAuth)
}

