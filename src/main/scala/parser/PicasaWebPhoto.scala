package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.Thumbnail

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object PicasaWebPhoto {
  
  def prefixGPhoto(nodes: NodeSeq) = nodes.filter(_.prefix == "gphoto").text

  def toThumbnail(item: Node): Thumbnail = {
    val url = (item \ "@url").text
    val height = (item \ "@height").text.toInt
    val width = (item \ "@width").text.toInt

    Thumbnail(url, width max height)
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
    thumbnails = (item \\ "thumbnail").map(toThumbnail).toList
  )

  def fromXML(items: NodeSeq) = (items \\ "entry").map(apply).toList
}

