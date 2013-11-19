package org.bone.sphotoapi.api

import org.bone.sphotoapi.oauth._
import org.bone.sphotoapi.parser._
import org.bone.sphotoapi.model._
import org.bone.sphotoapi.scribe._

import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.Google2Api
import org.scribe.model.Verb
import org.scribe.model.Verifier

import net.liftweb.json.JsonParser

import scala.util.Try
import java.util.Date

/**
 *  ImgUr API
 *
 *  Use this class to access ImgUr API, you should create it
 *  by calling method in ImgUrAPI companion object.
 *  
 *  @param  imgUrOAuth  ImgUrOAuth access object
 */
class ImgUrAPI private(imgUrOAuth: ImgUrOAuth) 
{
  /**
   *  Get current refresh token of ImgUr API
   *
   *  You could use refreshToken to verify ImgUr authorization,
   *  instead of direct user to ImgUr authorization page again.
   *
   *  @return   Current refresh token
   */
  def getRefreshToken = imgUrOAuth.refreshToken

  /**
   *  Get authorization URL
   *
   *  @return   The ImgUr authorization page if success.
   */
  def getAuthorizationURL: Try[String] = Try(imgUrOAuth.service.getAuthorizationUrl(null))

  /**
   *  Authorize ImgUr
   *
   *  @param  verifyCode  The pin / code returned by ImgUr
   *  @return             Success[Unit] if success.
   */
  def authorize(verifyCode: String): Try[Unit] = Try {
    
    val currentTime = System.currentTimeMillis
    val verifier = new Verifier(verifyCode)
    val accessToken = imgUrOAuth.service.getAccessToken(null, verifier)
    val jsonResponse = JsonParser.parse(accessToken.getRawResponse)

    val (rawAccessToken, refreshToken, expiresAt) = ImgUrOAuth.parseTokenJSON(jsonResponse)

    imgUrOAuth.accessToken = Some(accessToken)
    imgUrOAuth.refreshToken = Some(refreshToken)
    imgUrOAuth.expireAt = new Date(currentTime + expiresAt * 1000)
  }

  /**
   *  Get Photos From Album
   *
   *  @param    albumID     The ID of album
   *  @return               Success[List[Photo]] if everything is fine.
   */
  def getPhotos(albumID: String): Try[List[Photo]] = {

    val endPoint = s"3/album/$albumID/images.xml"

    imgUrOAuth.sendRequest(endPoint, Verb.GET).map { response =>
      ImgUrPhoto.fromXML(response.left.get)
    }

  }

  /**
   *  Get User's Album list
   *
   *  @param    username    username, "me" if you want to fetch current user's alubm.
   *  @return               Success[List[Album]] if everything is fine.
   */
  def getAlbums(username: String = "me", 
                page: Option[Int] = None, 
                perPage: Option[Int] = None): Try[List[Album]] = {

    val endPoint = s"3/account/$username/albums.xml"
    val params = (
      page.map("page" -> _.toString) ++ 
      perPage.map("perPage" -> _.toString)
    ).toList

    imgUrOAuth.sendRequest(endPoint, Verb.GET, params: _*).map { response =>
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
   *  @param    endPointPrefix    The app endpoint prefix
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
   *  @param    endPointPrefix    The app endpoint prefix
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
   *  @param    endPointPrefix    The app endpoint prefix
   *  @return                     ImgUrAPI object
   */
  def withRefreshToken(appKey: String, appSecret: String, 
                       refreshToken: String,
                       endPointPrefix: String = "https://api.imgur.com/"): ImgUrAPI = 
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
  private[api] def withMock(mockOAuth: ImgUrOAuth with MockImgUrOAuth) = {
    new ImgUrAPI(mockOAuth)
  }


}

