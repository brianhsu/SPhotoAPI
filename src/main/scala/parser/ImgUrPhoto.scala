package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.Thumbnail

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object ImgUrPhoto {
  
  def thumbnailURL(imageURL: String)(sizeInfo: (String, Int)) = {
    val (postfix, size) = sizeInfo
    val lastDotIndex = imageURL.lastIndexOf(".")
    Thumbnail(imageURL.take(lastDotIndex) + postfix + imageURL.drop(lastDotIndex), size)
  }

  def apply(item: Node): Photo = {

    val id = (item \ "id").text
    val imageURL = (item \ "link").text
    val thumbnailList = List("t" -> 160, "m" -> 320, "l" -> 640, "h" -> 1024).map(thumbnailURL(imageURL))
    val datetime = new Date((item \ "datetime").text.toLong * 1000)

    new Photo(
      id = id,
      title = Option((item \ "title").text).filterNot(_.isEmpty),
      timestamp = datetime,
      mimeType = (item \ "type").text,
      width = (item \ "width").text.toInt,
      height = (item \ "height").text.toInt,
      link = "http://imgur.com/" + id,
      imageURL = imageURL,
      thumbnails = thumbnailList,
      lastUpdated = datetime
    )
  }

  def fromXML(items: NodeSeq) = (items \\ "item").map(apply).toList
}

