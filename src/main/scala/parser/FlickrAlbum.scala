package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.AlbumPrivacy
import org.bone.sphotoapi.model.Album

import scala.xml.NodeSeq
import scala.xml.Node

import java.util.Date

object FlickrAlbum {
  
  def apply(username: String, item: Node): Album = {
    val albumID = (item \ "@id").text
    val linkURL = s"http://www.flickr.com/photos/$username/sets/${albumID}/"

    new Album(
      id          = albumID,
      title       = (item \ "title").text,
      description = Option((item \ "description").text).filterNot(_.isEmpty),
      dateTime    = new Date((item \ "@date_create").text.toLong * 1000),
      coverURL    = (item \\ "primary_photo_extras" \\ "@url_m").text,
      privacy     = AlbumPrivacy.Public,
      link        = linkURL
    )
  }

  def fromXML(username: String, items: NodeSeq) = (items \\ "photoset").map(apply(username, _)).toList
}

