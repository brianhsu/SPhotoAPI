package org.bone.sphotoapi.api

import org.bone.sphotoapi.oauth._
import org.bone.sphotoapi.parser._
import org.bone.sphotoapi.model._
import org.bone.sphotoapi.scribe._

import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.FlickrApi
import org.scribe.model.Verb

import scala.util.Try
import java.util.Date
import net.liftweb.json.JsonParser
import net.liftweb.json.JsonAST._

/**
 *  Flickr API
 *
 *  Use this class to access Flickr API, you should create it
 *  by calling method in FlickrAPI companion object.
 *  
 *  @param  picasaWebOAuth  FlickrOAuth access object
 */
class FlickrAPI private(override val oauth: FlickrOAuth) extends API(oauth, "Flickr")
{
  import FlickrAPI._

  private var cachedUsername: Option[String] = None

  private def getUsername = {
    
    if (cachedUsername.isDefined) {
      cachedUsername.get
    } else {
      
      val username = getUserInfo.get._1
      this.cachedUsername = Some(username)

      username
    }

  }

  /**
   *  Get Photos From Album
   *
   *  @param    albumID     The ID of album
   *  @param    userID      The ID of album's owner
   *  @return               Success[List[Photo]] if everything is fine.
   */
  def getPhotos(albumID: String): Try[List[Photo]] = Try
  {

    val ownerID = {
      oauth.sendRequest("/rest/", Verb.GET, "method" -> "flickr.photosets.getInfo", "photoset_id" -> albumID).map { response =>
        (response \\ "onwer").text
      }.get
    }
   
    val params = List(
      "method" -> "flickr.photosets.getPhotos",
      "photoset_id" -> albumID,
      "extras" -> "date_upload,date_taken,last_update,geo,url_sq, url_t, url_s, url_m, url_o"
    )

    oauth.sendRequest("rest", Verb.GET, params:_*).map { response =>
      FlickrPhoto.fromXML(ownerID, albumID, response)
    }.get
  }


  /**
   *  Get User's Album list
   *
   *  @param    userID   userID of album onwer, "default" if you want to fetch current user's alubm.
   *  @return            Success[List[Album]] if everything is fine.
   */
  def getAlbums(userID: String): Try[List[Album]] = {

    var params = List(
      "method" -> "flickr.photosets.getList",
      "primary_photo_extras" -> "url_m"
    )

    if (userID != "") {
      params ::= ("user_id" -> userID)
    }

    val username = if (userID != "") userID else this.getUsername
  
    oauth.sendRequest("rest", Verb.GET, params:_*).map { response =>
      FlickrAlbum.fromXML(username, response)
    }
  }

  def getAlbums() = getAlbums("")



  /**
   *  Get user information
   *
   *  @return   Try[(google UserID, EMail)]
   */
  override def getUserInfo: Try[(String, String)] = {

    oauth.sendRequest("rest", Verb.GET, "method" -> "flickr.test.login").map { response =>
      ((response \\ "@id").text, null)
    }

  }

}

/**
 *  Flickr API
 *
 */
object FlickrAPI {

  /**
   *  Create Flickr API that verify by user enter PIN directly.
   *
   *  @param    appKey            The app key you got from Flickr.
   *  @param    appSecret         The app secret you got from Flickr.
   *  @return                     FlickrAPI object
   */
  def apply(appKey: String, appSecret: String): FlickrAPI = 
  {

    val service = (new ServiceBuilder).
                    provider(classOf[FlickrApi]).
                    apiKey(appKey).
                    apiSecret(appSecret).
                    build

    val oauth = new FlickrOAuth(appKey, appSecret, service, accessToken = None)

    new FlickrAPI(oauth)
  }

  /**
   *  Create Flickr API that has callback.
   *
   *  @param    appKey            The app key you got from Flickr.
   *  @param    appSecret         The app secret you got from Flickr.
   *  @param    callback          The callback URL
   *  @return                     FlickrAPI object
   */
  def withCallback(appKey: String, appSecret: String, callback: String): FlickrAPI = 
  {

    val service = (new ServiceBuilder).
                    provider(classOf[FlickrApi]).
                    apiKey(appKey).
                    apiSecret(appSecret).
                    callback(callback).build

    val oauth = new FlickrOAuth(appKey, appSecret, service, accessToken = None)

    new FlickrAPI(oauth)
  }

  /**
   *  Used for Unit-test only.
   */
  private[api] def withMock(mockOAuth: FlickrOAuth with MockOAuth) = new FlickrAPI(mockOAuth)

}
