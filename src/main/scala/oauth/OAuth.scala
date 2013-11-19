package org.bone.sphotoapi.oauth

import org.scribe.model.Verb
import org.scribe.model.OAuthRequest

trait OAuth {

  protected def prefixURL: String

  /**
   *  Create OAuthReqeust object and attatch params to it.
   *
   *  @param    url         API Endpoint.
   *  @param    method      HTTP method type.
   *  @param    params      Parameters to send.
   */
  protected def buildRequest(url: String, method: Verb, 
                             params: (String, String)*): OAuthRequest = 
  {

    val fullURL = if(url.startsWith("http")) url else prefixURL + url
    val request = new OAuthRequest(method, fullURL)

    if (method == Verb.POST) {
      params.foreach { case(key, value) => request.addBodyParameter(key, value) }
    } else if (method == Verb.GET) {
      params.foreach { case(key, value) => request.addQuerystringParameter(key, value) }
    }

    request
  }

}

