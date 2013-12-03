package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.GPSPoint
import org.bone.sphotoapi.model.Thumbnail

import scala.xml.NodeSeq
import scala.xml.Node
import scala.util.Try

import java.util.Date
import java.text.SimpleDateFormat

object PicasaWebPhoto {
  
  val dateFormatter = new SimpleDateFormat("""yyyy-MM-dd'T'HH:mm:ss.SSS'Z'""")

  def prefixGPhoto(nodes: NodeSeq) = nodes.filter(_.prefix == "gphoto").text

  def toThumbnail(item: Node): Thumbnail = {
    val url = (item \ "@url").text
    val height = (item \ "@height").text.toInt
    val width = (item \ "@width").text.toInt

    Thumbnail(url, width max height)
  }

  def parseDate(dateTime: String) = dateFormatter.parse(dateTime)
  def parseGPS(posInfo: String) = Try {
    val List(lat, long) = posInfo.split(" ").toList
    GPSPoint(lat.toDouble, long.toDouble)
  }

  def apply(item: Node): Photo = new Photo(
    id = prefixGPhoto(item \ "id"),
    title = Option((item \ "summary").text).filterNot(_.isEmpty),
    timestamp = new Date(prefixGPhoto(item \ "timestamp").toLong),
    mimeType = (item \ "content" \ "@type").text,
    width = prefixGPhoto(item \ "width").toInt,
    height = prefixGPhoto(item \ "height").toInt,
    link = (item \ "link").filter(link => (link \ "@rel").text == "alternate").map(_ \ "@href").mkString,
    imageURL = (item \ "content" \ "@src").text,
    thumbnails = (item \\ "thumbnail").map(toThumbnail).toList,
    lastUpdated = parseDate((item \ "updated").text),
    location = parseGPS((item \\ "pos").text).toOption
  )

  def fromXML(items: NodeSeq) = (items \\ "entry").map(apply).toList
}

