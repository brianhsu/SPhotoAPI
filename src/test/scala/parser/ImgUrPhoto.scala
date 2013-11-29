package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo

import org.scalatest.FunSpec
import org.scalatest.Matchers

import java.util.Date

class ImgUrPhotoSpec extends FunSpec with Matchers {

  import ImgUrPhotoSpec._

  describe("ImgUrPhotoSpec") {
    it ("should parse photo with title XML correctly") {
      ImgUrPhoto(photoWithTitle) shouldBe Photo(
        id = "ssidO",
        title = Some("PhotoTitle1"),
        timestamp = new Date(1285674177 * 1000L),
        mimeType = "image/jpeg",
        width = 1144,
        height = 1600,
        link = "http://imgur.com/ssidO",
        imageURL = "http://i.imgur.com/ssidO.jpg"
      )
    }

    it ("should parse photo list XML correctly") {
      ImgUrPhoto.fromXML(photoList) shouldBe List(
        Photo(
          id = "ssidO",
          title = Some("PhotoTitle1"),
          timestamp = new Date(1285674177 * 1000L),
          mimeType = "image/jpeg",
          width = 1144,
          height = 1600,
          link = "http://imgur.com/ssidO",
          imageURL = "http://i.imgur.com/ssidO.jpg"
        ),
        Photo(
          id = "8jUywIU",
          title = None,
          timestamp = new Date(1365764935 * 1000L),
          mimeType = "image/jpeg",
          width = 1890,
          height = 2832,
          link = "http://imgur.com/8jUywIU",
          imageURL = "http://i.imgur.com/8jUywIU.jpg"
        ),
        Photo(
          id = "w5BqcnG",
          title = None,
          timestamp = new Date(1365764958 * 1000L),
          mimeType = "image/jpeg",
          width = 2832,
          height = 1890,
          link = "http://imgur.com/w5BqcnG",
          imageURL = "http://i.imgur.com/w5BqcnG.jpg"
        )
      )
    }
  }
}

object ImgUrPhotoSpec extends FunSpec with Matchers {
  val photoWithTitle = 
    <item>
      <id>ssidO</id>
      <title>PhotoTitle1</title>
      <description/>
      <datetime>1285674177</datetime>
      <type>image/jpeg</type>
      <animated>false</animated>
      <width>1144</width>
      <height>1600</height>
      <size>497154</size>
      <views>2042</views>
      <bandwidth>1015188468</bandwidth>
      <favorite>false</favorite>
      <link>http://i.imgur.com/ssidO.jpg</link>
    </item>

  val photoList = 
    <data status="200" success="1">
      <item>
        <id>ssidO</id>
        <title>PhotoTitle1</title>
        <description/>
        <datetime>1285674177</datetime>
        <type>image/jpeg</type>
        <animated>false</animated>
        <width>1144</width>
        <height>1600</height>
        <size>497154</size>
        <views>2042</views>
        <bandwidth>1015188468</bandwidth>
        <favorite>false</favorite>
        <link>http://i.imgur.com/ssidO.jpg</link>
      </item>
      <item>
        <id>8jUywIU</id>
        <title/>
        <description/>
        <datetime>1365764935</datetime>
        <type>image/jpeg</type>
        <animated>false</animated>
        <width>1890</width>
        <height>2832</height>
        <size>445218</size>
        <views>749</views>
        <bandwidth>333468282</bandwidth>
        <favorite>false</favorite>
        <link>http://i.imgur.com/8jUywIU.jpg</link>
      </item>
      <item>
        <id>w5BqcnG</id>
        <title/>
        <description/>
        <datetime>1365764958</datetime>
        <type>image/jpeg</type>
        <animated>false</animated>
        <width>2832</width>
        <height>1890</height>
        <size>448724</size>
        <views>290</views>
        <bandwidth>130129960</bandwidth>
        <favorite>false</favorite>
        <link>http://i.imgur.com/w5BqcnG.jpg</link>
      </item>
    </data>

}
