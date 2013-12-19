package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Album
import org.bone.sphotoapi.model.AlbumPrivacy

import org.scalatest.FunSpec
import org.scalatest.Matchers

import java.util.Date

class FlickrAlbumSpec extends FunSpec with Matchers {

  import FlickrAlbumSpec._

  describe("A FlickrAlbum Parser") {

    it ("should parse public Flickr AlbumXML correctly") {

      FlickrAlbum("username", PublicAlbumXML) shouldBe Album(
        id = "72157638817924753",
        title = "TestAlbum",
        description = Some("TestDescription"),
        dateTime = new Date(1387438878 * 1000L),
        coverURL = "http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b.jpg",
        privacy = AlbumPrivacy.Public,
        link = "http://www.flickr.com/photos/username/sets/72157638817924753/"
     )

    }

    it ("should parse album list correctly") {
      val albums = FlickrAlbum.fromXML("username", AlbumList)

      albums shouldBe List(
        Album(
          id = "0001",
          title = "TestAlbum",
          description = Some("TestDescription"),
          dateTime = new Date(1387438878 * 1000L),
          coverURL = "http://farm8.staticflickr.com/7364/1.jpg",
          privacy = AlbumPrivacy.Public,
          link = "http://www.flickr.com/photos/username/sets/0001/"
        ),
        Album(
          id = "0002",
          title = "TestAlbum2",
          description = Some("TestDescription2"),
          dateTime = new Date(1387438878 * 1000L),
          coverURL = "http://farm8.staticflickr.com/7364/2.jpg",
          privacy = AlbumPrivacy.Public,
          link = "http://www.flickr.com/photos/username/sets/0002/"
        )
      )
    }

  }
}

object FlickrAlbumSpec {

  val PublicAlbumXML = 
    <photoset id="72157638817924753" primary="11447097446" photos="5" videos="0" needs_interstitial="0" visibility_can_see_set="1" count_views="0" count_comments="0" can_comment="1" date_create="1387438878" date_update="1387441192">
      <title>TestAlbum</title>
      <description>TestDescription</description>
      <primary_photo_extras url_m="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b.jpg" height_m="333" width_m="500" />
    </photoset>

  val AlbumList =
    <rsp stat="ok">
      <photosets cancreate="1" page="1" pages="1" perpage="4" total="4">
        <photoset id="0001" primary="11447097446" photos="5" videos="0" needs_interstitial="0" visibility_can_see_set="1" count_views="0" count_comments="0" can_comment="1" date_create="1387438878" date_update="1387441192">
          <title>TestAlbum</title>
          <description>TestDescription</description>
          <primary_photo_extras url_m="http://farm8.staticflickr.com/7364/1.jpg" height_m="333" width_m="500" />
        </photoset>
        <photoset id="0002" primary="11447097446" photos="5" videos="0" needs_interstitial="0" visibility_can_see_set="1" count_views="0" count_comments="0" can_comment="1" date_create="1387438878" date_update="1387441192">
          <title>TestAlbum2</title>
          <description>TestDescription2</description>
          <primary_photo_extras url_m="http://farm8.staticflickr.com/7364/2.jpg" height_m="333" width_m="500" />
        </photoset>

      </photosets>
    </rsp>
}
