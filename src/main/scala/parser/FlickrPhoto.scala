package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.GPSPoint

import org.bone.sphotoapi.model.Thumbnail

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date
import java.text.SimpleDateFormat

object FlickrPhoto {
  
  def parseTakenDate(dateString: String) = {
    val dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    dateFormatter.parse(dateString)
  }

  def parseThumbnail(postfix: String, item: Node) = {
    val thumbnailURL = (item \ s"@url_$postfix").text
    val width = (item \ s"@width_$postfix").text.toInt
    val height = (item \ s"@height_$postfix").text.toInt
    Thumbnail(thumbnailURL, width max height)
  }

  def parseGPSPoint(item: Node) = {
    val isPublic = (item \ "@geo_is_public").text == "1"
    
    isPublic match {
      case true  => Some(GPSPoint((item \ "@latitude").text.toDouble, (item \ "@longitude").text.toDouble))
      case false => None
    }
  }

  def apply(username: String, albumID: String, item: Node): Photo = {

    val id = (item \ "@id").text
    val linkURL = s"http://www.flickr.com/photos/$username/$id/in/set-$albumID"
    val thumbnailList = List("sq", "t", "s", "m").map(parseThumbnail(_, item))

    new Photo(
      id = id,
      title = Option((item \ "@title").text).filterNot(_.isEmpty),
      timestamp = parseTakenDate((item \ "@datetaken").text),
      mimeType = "image/jpeg",
      width = (item \ "@width_o").text.toInt,
      height = (item \ "@height_o").text.toInt,
      link = linkURL,
      imageURL = (item \ "@url_o").text,
      thumbnails = thumbnailList,
      lastUpdated = new Date((item \ "@lastupdate").text.toLong * 1000L),
      location = parseGPSPoint(item)
    )
  }

  def fromXML(username: String, albumID: String, items: NodeSeq) = (items \\ "photo").map(apply(username, albumID, _)).toList
}

