package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Album
import org.bone.sphotoapi.model.AlbumPrivacy

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

import java.util.Date

class ImgUrAlbumSpec extends FunSpec with ShouldMatchers {

  import ImgUrAlbumSpec._

  describe("A ImgUrAlbum Parser") {

    it ("should parse public ImgUr AlbumXML correctly") {

      ImgUrAlbum(PublicAlbumXML) should be === Album(
        id = "AHwna",
        title = "怦然心動的人生整理魔法",
        description = None,
        dateTime = new Date(1351248155 * 1000L),
        coverURL = "http://i.imgur.com/OXqucb.jpg",
        privacy = AlbumPrivacy.Public,
        link = "http://imgur.com/a/AHwna"
     )

    }

    it ("should parse hidden ImgUr AlbumXML correctly") {

      ImgUrAlbum(HiddenAlbumXML) should be === Album(
        id = "Azdxq",
        title = "test",
        description = Some("Hello Hidden Album"),
        dateTime = new Date(1367551388 * 1000L),
        coverURL = "http://i.imgur.com/ssidOb.jpg",
        privacy = AlbumPrivacy.Hidden,
        link = "http://imgur.com/a/Azdxq"
      )

    }

    it ("should parse secret ImgUr AlbumXML correctly") {

      ImgUrAlbum(SecretAlbumXML) should be === Album(
        id = "rXK1z",
        title = "MySecret",
        description = None,
        dateTime = new Date(1367302130 * 1000L),
        coverURL = "http://i.imgur.com/H7uAIQ3b.jpg",
        privacy = AlbumPrivacy.Private,
        link = "http://imgur.com/a/rXK1z"
      )

    }

    it ("should parse album list correctly") {
      val albums = ImgUrAlbum.fromXML(AlbumList)
      albums should be === List(
        Album(
          id = "Azdxq",
          title = "test",
          description = Some("Hello Hidden Album"),
          dateTime = new Date(1367551388 * 1000L),
          coverURL = "http://i.imgur.com/ssidOb.jpg",
          privacy = AlbumPrivacy.Hidden,
          link = "http://imgur.com/a/Azdxq"
        ),
        Album(
          id = "rXK1z",
          title = "MySecret",
          description = None,
          dateTime = new Date(1367302130 * 1000L),
          coverURL = "http://i.imgur.com/H7uAIQ3b.jpg",
          privacy = AlbumPrivacy.Private,
          link = "http://imgur.com/a/rXK1z"
        ),
        Album(
          id = "AHwna",
          title = "怦然心動的人生整理魔法",
          description = None,
          dateTime = new Date(1351248155 * 1000L),
          coverURL = "http://i.imgur.com/OXqucb.jpg",
          privacy = AlbumPrivacy.Public,
          link = "http://imgur.com/a/AHwna" 
        )
      )
    }

  }
}

object ImgUrAlbumSpec {

  val PublicAlbumXML = 
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

  val HiddenAlbumXML = 
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

  val SecretAlbumXML = 
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

  val AlbumList =
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
}
