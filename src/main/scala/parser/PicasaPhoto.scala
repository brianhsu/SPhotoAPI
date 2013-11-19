package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object PicasaPhoto {
  
  def prefixGPhoto(nodes: NodeSeq) = nodes.filter(_.prefix == "gphoto").text

  def apply(item: Node): Photo = new Photo(
    id = prefixGPhoto(item \ "id"),
    title = Option((item \ "summary").text).filterNot(_.isEmpty),
    timestamp = new Date(prefixGPhoto(item \ "timestamp").toLong),
    mimeType = (item \ "content" \ "@type").text,
    width = prefixGPhoto(item \ "width").toInt,
    height = prefixGPhoto(item \ "height").toInt,
    link = (item \ "content" \ "@src").text
  )

  def fromXML(items: NodeSeq) = (items \\ "entry").map(apply).toList
}

