package org.scribe.builder.api 

import org.scribe.model.Token

class PhotobucketApi extends DefaultApi10a {
  override def getAccessTokenEndpoint() = "http://api.photobucket.com/login/access"
  override def getRequestTokenEndpoint() = "http://api.photobucket.com/login/request"
  override def getAuthorizationUrl(requestToken: Token) = {
    s"http://photobucket.com/apilogin/login?oauth_token=${requestToken.getToken}" 
  }
}
