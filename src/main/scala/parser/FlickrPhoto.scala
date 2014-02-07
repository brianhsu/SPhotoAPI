package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.GPSPoint

import org.bone.sphotoapi.model.Thumbnail

import scala.xml.NodeSeq
import scala.xml.Node
import scala.util.Try

import java.util.Date
import java.text.SimpleDateFormat

object FlickrPhoto {
  
  def parseTakenDate(dateString: String) = {
    val dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    dateFormatter.parse(dateString)
  }

  def parseThumbnail(postfix: String, item: Node) = Try {
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

  def apply(username: String, basicInfo: Node, sizeInfo: Node, geoInfo: Node): Photo = {

    val id = (basicInfo \\ "id").text
    val dates = basicInfo \\ "dates"
    val origSize = (sizeInfo \\ "size").filter(node => (node \\ "@label").text == "Original")
    val thumbnailList = (sizeInfo \\ "size").toList map { thumbnail =>
      val url = (thumbnail \ "@source").text
      val width = (thumbnail \ "@width").text.toInt
      val height = (thumbnail \ "@height").text.toInt
      new Thumbnail(url, width max height)
    }

    val geoPoint = for {
      location <- geoInfo \\ "@location"
      latitude <- (location \\ "@latitude")
      longitude <- (location \\ "@longitude")
    } yield GPSPoint(latitude.text.toDouble, longitude.text.toDouble)

    new Photo(
      id = id,
      title = Option((basicInfo \ "title").text).filterNot(_.isEmpty),
      timestamp = parseTakenDate((dates \ "@taken").text),
      mimeType = "image/jpeg",
      width = (origSize \ "@width").text.toInt,
      height = (origSize \ "@height").text.toInt,
      link = (basicInfo \\ "url").text,
      imageURL = (origSize \ "@url").text,
      thumbnails = thumbnailList,
      lastUpdated = new Date((dates \ "@lastupdate").text.toLong * 1000L),
      location = geoPoint.headOption
    )

  }

  def apply(username: String, albumID: String, item: Node): Photo = {

    val id = (item \ "@id").text
    val linkURL = s"http://www.flickr.com/photos/$username/$id/in/set-$albumID"
    val thumbnailList = List(
      parseThumbnail("sq", item),
      parseThumbnail("t", item),
      parseThumbnail("s", item),
      parseThumbnail("m", item)
    ).filter(_.isSuccess).map(_.get)

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

