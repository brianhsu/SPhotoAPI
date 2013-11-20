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

abstract class API(val oauth: OAuth)
{
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
  def getAuthorizationURL: Try[String] = Try(oauth.service.getAuthorizationUrl(null))

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

