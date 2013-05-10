package org.bone.sphotoapi.model

sealed trait AlbumPrivacy

object AlbumPrivacy {
  case object Public extends AlbumPrivacy
  case object Hidden extends AlbumPrivacy
  case object Private extends AlbumPrivacy
}
