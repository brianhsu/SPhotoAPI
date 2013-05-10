package org.bone.sphotoapi.model

import java.util.Date

case class Album(
  id: String, title: String, description: Option[String], 
  dateTime: Date, coverURL: String, privacy: AlbumPrivacy, link: String
)

