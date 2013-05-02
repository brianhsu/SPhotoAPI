package org.bone.sphoto.scribe

import org.scribe.builder.api.DefaultApi20

import org.scribe.model.OAuthConfig
import org.scribe.oauth.OAuth20ServiceImpl
import org.scribe.builder.api._
import org.scribe.model._
import org.scribe.extractors.JsonTokenExtractor

class ImgUr3Service(api: DefaultApi20, 
                    config: OAuthConfig) extends OAuth20ServiceImpl(api, config) {

  private def isOOB(config: OAuthConfig) = "oob" == config.getCallback

  override def getAccessToken(requestToken: Token, verifier: Verifier): Token = {

    val request = new OAuthRequest(api.getAccessTokenVerb, api.getAccessTokenEndpoint)
    val grantType = if (isOOB(config)) "pin" else "authorization_code"

    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey)
    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret)
    request.addBodyParameter("grant_type", grantType)
    request.addBodyParameter("pin", verifier.getValue);

    val response = request.send()

    api.getAccessTokenExtractor().extract(response.getBody)
  }

  override def signRequest(accessToken: Token, request: OAuthRequest): Unit = {

    if (accessToken != null) {
      request.addHeader("Authorization", "Bearer " + accessToken.getToken)
    } else {
      request.addHeader("Authorization", "Client-ID " + config.getApiKey)
    }
  }

}

class ImgUr3Api extends DefaultApi20 {

  private val authorizeURL = "https://api.imgur.com/oauth2/authorize"

  private def isOOB(config: OAuthConfig) = "oob" == config.getCallback

  override def getAccessTokenVerb() = Verb.POST
  override def getAccessTokenExtractor() = new JsonTokenExtractor()
  override def getAccessTokenEndpoint() = "https://api.imgur.com/oauth2/token"

  override def getAuthorizationUrl(config: OAuthConfig) = {
    isOOB(config) match {
      case true  => s"${authorizeURL}?client_id=${config.getApiKey}&response_type=pin"
      case false => s"${authorizeURL}?client_id=${config.getApiKey}&response_type=code"
    }
  }

  override def createService(config: OAuthConfig) = {
    new ImgUr3Service(this, config)
  }
}
