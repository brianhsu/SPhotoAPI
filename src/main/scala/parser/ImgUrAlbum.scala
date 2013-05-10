package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.AlbumPrivacy
import org.bone.sphotoapi.model.Album

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object ImgUrAlbum {
  
  def getPrivacy(privacy: String): AlbumPrivacy = privacy match {
    case "public" => AlbumPrivacy.Public
    case "hidden" => AlbumPrivacy.Hidden
    case "secret" => AlbumPrivacy.Private
  }

  def coverImageURL(photoID: String) = s"http://i.imgur.com/${photoID}b.jpg"

  def apply(item: Node): Album = new Album(
    id          = (item \ "id").text,
    title       = (item \ "title").text,
    description = Option((item \ "description").text).filterNot(_.isEmpty),
    dateTime    = new Date((item \ "datetime").text.toLong * 1000),
    coverURL    = coverImageURL((item \ "cover").text),
    privacy     = getPrivacy((item \ "privacy").text),
    link        = (item \ "link").text
  )

  def fromXML(items: NodeSeq) = (items \\ "item").map(apply).toList
}

