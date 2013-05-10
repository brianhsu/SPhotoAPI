package org.bone.sphotoapi.oauth

class OAuthException(message: String, val rawResponse: String) extends Exception(message)

