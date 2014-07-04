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
import java.io._

case class PhotosetPage(photos: List[Photo], page: Int, totalPage: Int)

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

  override def uploadPhoto(photo: File): Try[Photo] = {
    import org.apache.http.entity.mime.MultipartEntityBuilder;
    import org.apache.http.entity.mime.content.FileBody;

    val bin = new FileBody(photo)
    val reqEntity = MultipartEntityBuilder.create().addPart("photo", bin).build
    val contentType = reqEntity.getContentType
    val bos = new ByteArrayOutputStream(reqEntity.getContentLength.toInt)
    reqEntity.writeTo(bos)

    val response = oauth.sendRequest(
      url = "https://up.flickr.com/services/upload/",
      verb = Verb.POST,
      payload = Some(bos.toByteArray),
      headers = Map(
        contentType.getName -> contentType.getValue
      )
    )

    for {
      content <- response
      photoID <- Try((content \\ "photoid").text)
      photo <- getPhoto(photoID)
    } yield photo

  }

  override def getPhoto(photoID: String): Try[Photo] = {

    def getBasicInfo = oauth.sendRequest(
      url = "rest", 
      verb = Verb.GET, 
      getParams = Map(
        "method" -> "flickr.photos.getInfo",
        "photo_id" -> photoID
      )
    )
    
    def getSizeInfo = oauth.sendRequest(
      url = "rest", 
      verb = Verb.GET, 
      getParams = Map(
        "method" -> "flickr.photos.getSizes",
        "photo_id" -> photoID
      )
    )

    def getGeoInfo = oauth.sendRequest(
      url = "rest", 
      verb = Verb.GET, 
      getParams = Map(
        "method" -> "flickr.photos.geo.getLocation",
        "photo_id" -> photoID
      )
    )

    for {
      basicInfo <- getBasicInfo
      sizeInfo <- getSizeInfo
      geoInfo <- getGeoInfo
    } yield {
      FlickrPhoto(getUsername, basicInfo, sizeInfo, geoInfo)
    }

  }

  /**
   *  Get Photos From Album
   *
   *  @param    albumID     The ID of album
   *  @param    userID      The ID of album's owner
   *  @return               Success[List[Photo]] if everything is fine.
   */
  def getPhotos(albumID: String, page: Int): Try[PhotosetPage] = Try
  {

    val ownerID = {

      val response = oauth.sendRequest(
        url = "/rest/", 
        verb = Verb.GET, 
        getParams = Map(
          "method" -> "flickr.photosets.getInfo", 
          "photoset_id" -> albumID
        )
      )
      
      response.map { content => (content \\ "onwer").text }.get
    }
   
    val response = oauth.sendRequest(
      url = "rest", 
      verb = Verb.GET, 
      getParams = Map(
        "method" -> "flickr.photosets.getPhotos",
        "photoset_id" -> albumID,
        "extras" -> "date_upload,date_taken,last_update,geo,url_sq, url_t, url_s, url_m, url_o",
        "page" -> page.toString
      )
    )
      
    response.map { content =>
      val photos = FlickrPhoto.fromXML(ownerID, albumID, content)
      val totalPage = (content \\ "@pages").text.toInt

      PhotosetPage(photos, page, totalPage)
    }.get

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
    val firstPage = getPhotos(albumID, 1).get
    var allPhotos = firstPage.photos

    for (page <- 2 to firstPage.totalPage) {
      allPhotos ++= getPhotos(albumID, page).get.photos
    }

    allPhotos
  }


  /**
   *  Get User's Album list
   *
   *  @param    userID   userID of album onwer, "default" if you want to fetch current user's alubm.
   *  @return            Success[List[Album]] if everything is fine.
   */
  def getAlbums(userID: String): Try[List[Album]] = {

    var params = Map(
      "method" -> "flickr.photosets.getList",
      "primary_photo_extras" -> "url_m"
    )

    if (userID != "") {
      params += ("user_id" -> userID)
    }

    val username = if (userID != "") userID else this.getUsername
  
    val response = oauth.sendRequest(
      url = "rest", 
      verb = Verb.GET, 
      getParams = params
    )
    
    response.map { content =>
      FlickrAlbum.fromXML(username, content)
    }
  }

  def getAlbums() = getAlbums("")



  /**
   *  Get user information
   *
   *  @return   Try[(google UserID, EMail)]
   */
  override def getUserInfo: Try[(String, String)] = {

    val response = oauth.sendRequest(
      url = "rest", 
      verb = Verb.GET, 
      getParams = Map(
        "method" -> "flickr.test.login"
      )
    )
    
    response.map { content => ((content \\ "@id").text, null) }
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
