package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.GPSPoint
import org.bone.sphotoapi.model.Thumbnail

import org.scalatest.FunSpec
import org.scalatest.Matchers

import java.util.Date

class FlickrPhotoSpec extends FunSpec with Matchers {

  import FlickrPhotoSpec._

  describe("FlickrPhotoSpec") {
    it ("should parse photo with title XML correctly") {
      FlickrPhoto("username", "albumID", photoWithTitle) shouldBe Photo(
        id = "11447097446",
        title = Some("TestTitle"),
        timestamp = new Date(1385254740000L),
        mimeType = "image/jpeg",
        width = 2048,
        height = 1363,
        link = "http://www.flickr.com/photos/username/11447097446/in/set-albumID",
        imageURL = "http://farm8.staticflickr.com/7364/11447097446_8564cf0cec_o.jpg",
        thumbnails = List(
          Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_s.jpg", 75),
          Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_t.jpg", 100),
          Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_m.jpg", 240),
          Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b.jpg", 500)
        ),
        lastUpdated = new Date(1387443826 * 1000L)
      )
    }

    it ("should parse photo list XML correctly") {
      FlickrPhoto.fromXML("username", "albumID", photoList) shouldBe List(
        Photo(
          id = "11447097446",
          title = Some("TestTitle"),
          timestamp = new Date(1385254740000L),
          mimeType = "image/jpeg",
          width = 2048,
          height = 1363,
          link = "http://www.flickr.com/photos/username/11447097446/in/set-albumID",
          imageURL = "http://farm8.staticflickr.com/7364/11447097446_8564cf0cec_o.jpg",
          thumbnails = List(
            Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_s.jpg", 75),
            Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_t.jpg", 100),
            Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_m.jpg", 240),
            Thumbnail("http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b.jpg", 500)
          ),
          lastUpdated = new Date(1387443826 * 1000L)
        ),
        Photo(
          id = "11446844164",
          title = Some("01"),
          timestamp = new Date(1387410060000L),
          mimeType = "image/jpeg",
          width = 398,
          height = 660,
          link = "http://www.flickr.com/photos/username/11446844164/in/set-albumID",
          imageURL = "http://farm4.staticflickr.com/3790/11446844164_fa96826f5b_o.png",
          thumbnails = List(
            Thumbnail("http://farm4.staticflickr.com/3790/11446844164_0891809545_s.jpg", 75), 
            Thumbnail("http://farm4.staticflickr.com/3790/11446844164_0891809545_t.jpg", 100),
            Thumbnail("http://farm4.staticflickr.com/3790/11446844164_0891809545_m.jpg", 240),
            Thumbnail("http://farm4.staticflickr.com/3790/11446844164_0891809545.jpg", 500)
          ),
          lastUpdated = new Date(1387438879 * 1000L)
        ),
        Photo(
          id = "11447097526",
          title = Some("02"),
          timestamp = new Date(1160530620000L),
          mimeType = "image/jpeg",
          width = 1024,
          height = 768,
          link = "http://www.flickr.com/photos/username/11447097526/in/set-albumID",
          imageURL = "http://farm3.staticflickr.com/2829/11447097526_49f34cbc93_o.jpg",
          thumbnails = List(
            Thumbnail("http://farm3.staticflickr.com/2829/11447097526_8050cc19b1_s.jpg", 75),
            Thumbnail("http://farm3.staticflickr.com/2829/11447097526_8050cc19b1_t.jpg", 75),
            Thumbnail("http://farm3.staticflickr.com/2829/11447097526_8050cc19b1_m.jpg", 180),
            Thumbnail("http://farm3.staticflickr.com/2829/11447097526_8050cc19b1.jpg", 375)
          ),
          lastUpdated = new Date(1387438879 * 1000L),
          location = Some(GPSPoint(-33.856087,151.219925))
        )
      )
    }
  }
}

object FlickrPhotoSpec extends FunSpec with Matchers {
  val photoWithTitle = 
    <photo id="11447097446" server="7364" farm="8" lastupdate="1387443826"
           title="TestTitle" isprimary="0" machine_tags="" latitude="0" longitude="0" accuracy="0" context="0" 
           dateupload="1387440279" datetaken="2013-11-24 08:59:39" datetakengranularity="0" 
           url_sq="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_s.jpg" height_sq="75" width_sq="75" 
           url_t="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_t.jpg" height_t="67" width_t="100" 
           url_s="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_m.jpg" height_s="160" width_s="240" 
           url_m="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b.jpg" height_m="333" width_m="500" 
           url_o="http://farm8.staticflickr.com/7364/11447097446_8564cf0cec_o.jpg" height_o="1363" width_o="2048" />

  val photoList = 
    <rsp stat="ok">
      <photoset id="72157638817924753" primary="11447097446" owner="91878952@N00" ownername="墳墓" page="1" per_page="500" perpage="500" pages="1" total="5" title="qqqq">
        <photo id="11447097446" server="7364" farm="8" lastupdate="1387443826"
               title="TestTitle" isprimary="0" machine_tags="" latitude="0" longitude="0" accuracy="0" context="0" 
               dateupload="1387440279" datetaken="2013-11-24 08:59:39" datetakengranularity="0" 
               url_sq="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_s.jpg" height_sq="75" width_sq="75" 
               url_t="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_t.jpg" height_t="67" width_t="100" 
               url_s="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b_m.jpg" height_s="160" width_s="240" 
               url_m="http://farm8.staticflickr.com/7364/11447097446_df4a6ef39b.jpg" height_m="333" width_m="500" 
               url_o="http://farm8.staticflickr.com/7364/11447097446_8564cf0cec_o.jpg" height_o="1363" width_o="2048" />
        <photo id="11446844164" server="3790" farm="4" title="01" isprimary="0" machine_tags="" 
               latitude="0" longitude="0" accuracy="0" context="0" lastupdate="1387438879"
               dateupload="1387438878" datetaken="2013-12-19 07:41:02" datetakengranularity="0" 
               url_sq="http://farm4.staticflickr.com/3790/11446844164_0891809545_s.jpg" height_sq="75" width_sq="75" 
               url_t="http://farm4.staticflickr.com/3790/11446844164_0891809545_t.jpg" height_t="100" width_t="60" 
               url_s="http://farm4.staticflickr.com/3790/11446844164_0891809545_m.jpg" height_s="240" width_s="145"
               url_m="http://farm4.staticflickr.com/3790/11446844164_0891809545.jpg" height_m="500" width_m="302"
               url_o="http://farm4.staticflickr.com/3790/11446844164_fa96826f5b_o.png" height_o="660" width_o="398" />
        <photo id="11447097526" server="2829" farm="3" title="02" isprimary="0" machine_tags="" 
               latitude="-33.856087" longitude="151.219925" accuracy="16" context="0" lastupdate="1387438879"
               place_id="G_1.ivRWULgY8g0f" woeid="7225657" geo_is_family="0" geo_is_friend="0" geo_is_contact="0" 
               geo_is_public="1" dateupload="1387440278" datetaken="2006-10-11 09:37:52" 
               datetakengranularity="0" 
               url_sq="http://farm3.staticflickr.com/2829/11447097526_8050cc19b1_s.jpg" height_sq="75" width_sq="75"
               url_t="http://farm3.staticflickr.com/2829/11447097526_8050cc19b1_t.jpg" height_t="75" width_t="100"
               url_s="http://farm3.staticflickr.com/2829/11447097526_8050cc19b1_m.jpg" height_s="180" width_s="240"
               url_m="http://farm3.staticflickr.com/2829/11447097526_8050cc19b1.jpg" height_m="375" width_m="500"
               url_o="http://farm3.staticflickr.com/2829/11447097526_49f34cbc93_o.jpg" height_o="768" width_o="1024" />
      </photoset>
    </rsp>
}
