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
import java.net.URLEncoder

abstract class API(val oauth: OAuth, val serviceName: String)
{
  /**
   *  Get Photos From Album
   *
   *  @param    albumID     The ID of album
   *  @return               Success[List[Photo]] if everything is fine.
   */
  def getPhotos(albumID: String): Try[List[Photo]]

  /**
   *  Get User's Album list
   *
   *  @return               Success[List[Album]] if everything is fine.
   */
  def getAlbums(): Try[List[Album]]

  /**
   *  Get user information
   *
   *  @return   Try[(google UserID, EMail)]
   */
  def getUserInfo(): Try[(String, String)]

  /**
   *  Get current refresh token of ImgUr API
   *
   *  You could use refreshToken to verify ImgUr authorization,
   *  instead of direct user to ImgUr authorization page again.
   *
   *  @return   Current refresh token
   */
  def getRefreshToken = oauth.refreshToken

  /**
   *  Get authorization URL
   *
   *  @return   The ImgUr authorization page if success.
   */
  def getAuthorizationURL(params: (String, String)*): Try[String] = Try {
    
    val urlParameter = params.map { case(name, value) => 
      val encodedName = URLEncoder.encode(name, "utf-8")
      val encodedValue = URLEncoder.encode(value, "utf-8")
      s"$encodedName=$encodedValue" 
    }.mkString("&", "&", "")

    params match {
      case Nil => oauth.service.getAuthorizationUrl(null)
      case _   => oauth.service.getAuthorizationUrl(null) + urlParameter
    }
  }

  /**
   *  Authorize ImgUr
   *
   *  @param  verifyCode  The pin / code returned by ImgUr
   *  @return             Success[Unit] if success.
   */
  def authorize(verifyCode: String): Try[Unit] = Try {
    
    val currentTime = System.currentTimeMillis
    val verifier = new Verifier(verifyCode)
    val accessToken = oauth.service.getAccessToken(null, verifier)
    val jsonResponse = JsonParser.parse(accessToken.getRawResponse)

    val (rawAccessToken, refreshToken, expiresAt) = OAuth.parseTokenJSON(jsonResponse)

    oauth.accessToken = Some(accessToken)
    oauth.refreshToken = refreshToken
    oauth.expireAt = new Date(currentTime + expiresAt * 1000)
  }

}

