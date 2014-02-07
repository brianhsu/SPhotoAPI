package org.bone.sphotoapi.api

import org.scalatest.FunSpec
import org.scalatest.Matchers

import org.bone.sphotoapi.oauth._

import org.scribe.model.Verb

import scala.util.Success
import scala.util.Failure

object ImgUrAPIMock extends ImgUrOAuth(null, null, null, null) with MockOAuth {
  
  override def sendRequest_(url: String, verb: Verb, 
                            getParams: Map[String, String] = Map.empty, 
                            postParams: Map[String, String] = Map.empty,
                            headers: Map[String, String] = Map.empty,
                            payload: Option[Array[Byte]]) = 
  {
    
    (url, verb) match {
      case ("3/account/test/albums.xml", Verb.GET) => Success((200, "text/xml", albumsXML.toString))
      case ("3/album/test/images.xml", Verb.GET) => Success((200, "text/xml", photosXML.toString))
      case _ => Success((400, "text/xml", "<error>Not Found</error>"))
    }

  }

  val albumsXML = 
    <data status="200" success="1">
      <item>
        <id>Azdxq</id>
        <title>test</title>
        <description>Hello Hidden Album</description>
        <datetime>1367551388</datetime>
        <cover>ssidO</cover>
        <account_url>brianhsu</account_url>
        <privacy>hidden</privacy>
        <layout>blog</layout>
        <views>3</views>
        <link>http://imgur.com/a/Azdxq</link>
        <favorite>false</favorite>
        <order>0</order>
      </item>
      <item>
        <id>rXK1z</id>
        <title>MySecret</title>
        <description/>
        <datetime>1367302130</datetime>
        <cover>H7uAIQ3</cover>
        <account_url>brianhsu</account_url>
        <privacy>secret</privacy>
        <layout>blog</layout>
        <views>4</views>
        <link>http://imgur.com/a/rXK1z</link>
        <favorite>false</favorite>
        <order>0</order>
      </item>
      <item>
        <id>AHwna</id>
        <title>怦然心動的人生整理魔法</title>
        <description/>
        <datetime>1351248155</datetime>
        <cover>OXquc</cover>
        <account_url>brianhsu</account_url>
        <privacy>public</privacy>
        <layout>horizontal</layout>
        <views>2805</views>
        <link>http://imgur.com/a/AHwna</link>
        <favorite>false</favorite>
        <order>0</order>
      </item>
    </data>

  val photosXML = 
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

class ImgUrAPISpec extends FunSpec with Matchers {

  val api = ImgUrAPI.withMock(ImgUrAPIMock)

  describe("ImgUrAPI") {

    it("get exist user's album list correctly") {
      api.getAlbums("test").get.map(_.id) shouldBe List("Azdxq", "rXK1z", "AHwna")
    }

    it("get exist albums's photo list correctly") {
      api.getPhotos("test").get.map(_.id) shouldBe List("ssidO", "8jUywIU", "w5BqcnG")
    }

    it("return Failure when user not found") {
      api.getAlbums("userNotFound").isFailure shouldBe true
    }

    it("return Failure when album not found") {
      api.getAlbums("photoNotFound").isFailure shouldBe true
    }

  }
}
