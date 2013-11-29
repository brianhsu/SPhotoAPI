package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object ImgUrPhoto {
  
  def apply(item: Node): Photo = new Photo(
    id = (item \ "id").text,
    title = Option((item \ "title").text).filterNot(_.isEmpty),
    timestamp = new Date((item \ "datetime").text.toLong * 1000),
    mimeType = (item \ "type").text,
    width = (item \ "width").text.toInt,
    height = (item \ "height").text.toInt,
    link = "http://imgur.com/" + (item \ "id").text,
    imageURL = (item \ "link").text
  )

  def fromXML(items: NodeSeq) = (items \\ "item").map(apply).toList
}

