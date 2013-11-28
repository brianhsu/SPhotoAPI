package org.bone.sphotoapi.model

import java.util.Date

case class Photo(
  id: String, title: Option[String], timestamp: Date, mimeType: String,
  width: Int, height: Int,
  link: String, imageURL: String
)

