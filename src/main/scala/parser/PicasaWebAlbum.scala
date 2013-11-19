package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.AlbumPrivacy
import org.bone.sphotoapi.model.Album

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object PicasaWebAlbum {
  
  def prefixGPhoto(nodes: NodeSeq) = nodes.filter(_.prefix == "gphoto").text

  def getPrivacy(privacy: String): AlbumPrivacy = privacy match {
    case "public" => AlbumPrivacy.Public
    case "private" => AlbumPrivacy.Hidden
    case "protected" => AlbumPrivacy.Private
  }

  def apply(item: Node): Album = new Album(
    id          = prefixGPhoto((item \ "id")),
    title       = (item \ "title").text,
    description = Option((item \ "summary").text).filterNot(_.isEmpty),
    dateTime    = new Date(prefixGPhoto((item \ "timestamp")).toLong),
    coverURL    = (item \\ "content" \\ "@url").text,
    privacy     = getPrivacy(prefixGPhoto((item \ "access"))),
    link        = ((item \ "link").filter(link => (link \ "@type").text == "text/html") \ "@href").text
  )

  def fromXML(items: NodeSeq) = (items \\ "entry").map(apply).toList
}

