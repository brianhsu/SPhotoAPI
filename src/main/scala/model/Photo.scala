package org.bone.sphotoapi.model

import java.util.Date

case class GPSPoint(latitude: Double, longitude: Double)

case class Photo(
  id: String, title: Option[String], timestamp: Date, mimeType: String,
  width: Int, height: Int,
  link: String, imageURL: String,
  thumbnails: List[Thumbnail] = Nil,
  lastUpdated: Date,
  location: Option[GPSPoint] = None
)

