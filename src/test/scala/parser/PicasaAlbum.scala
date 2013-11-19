package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Album
import org.bone.sphotoapi.model.AlbumPrivacy

import org.scalatest.FunSpec
import org.scalatest.Matchers

import java.util.Date

class PicasaAlbumSpec extends FunSpec with Matchers {

  import PicasaAlbumSpec._

  describe("A PicasaAlbum Parser") {

    it ("should parse public Picasa AlbumXML correctly") {

      PicasaAlbum(PublicAlbumXML) shouldBe Album(
        id = "5910634723613201729",
        title = "SomeTitle",
        description = None,
        dateTime = new Date(1376176887000L),
        coverURL = "https://lh6.googleusercontent.com/-H5vGEpc8LdE/UgbK9xoN1UE/AAAAAAAATM0/9PmM99XYmN4/21030811.jpg",
        privacy = AlbumPrivacy.Public,
        link = "https://picasaweb.google.com/106826117950796832071/21030811"
     )

    }

    it ("should parse hidden Picasa AlbumXML correctly") {

      PicasaAlbum(HiddenAlbumXML) shouldBe Album(
        id = "5836947895216307969",
        title = "TestTitle",
        description = Some("TestDescription"),
        dateTime = new Date(1644825600000L),
        coverURL = "https://lh4.googleusercontent.com/-xjDKIT-urHE/UQEBL2rU_wE/AAAAAAAATTo/51wqrF0sHj8/NewLovePlus.jpg",
        privacy = AlbumPrivacy.Hidden,
        link = "https://picasaweb.google.com/106826117950796832071/NewLovePlus?authkey=Gv1sRgCI6bvoj4rZWJzgE"
      )

    }

    it ("should parse secret Picasa AlbumXML correctly") {

      PicasaAlbum(SecretAlbumXML) shouldBe Album(
        id = "5778560919691441265",
        title = "PrivateTest",
        description = None,
        dateTime = new Date(1672473600000L),
        coverURL = "https://lh4.googleusercontent.com/-E9TOZCEsi54/UDGSilMptHE/AAAAAAAAS1U/zXHN7L1-S0M/mxYgBC.jpg",
        privacy = AlbumPrivacy.Private,
        link = "https://picasaweb.google.com/106826117950796832071/mxYgBC"
      )

    }

    it ("should parse album list correctly") {
      val albums = PicasaAlbum.fromXML(AlbumList)
      albums shouldBe List(
        Album(
          id = "5910634723613201729",
          title = "Test1",
          description = None,
          dateTime = new Date(1376176887000L),
          coverURL = "https://lh6.googleusercontent.com/-H5vGEpc8LdE/UgbK9xoN1UE/AAAAAAAATM0/9PmM99XYmN4/21030811.jpg",
          privacy = AlbumPrivacy.Private,
          link = "https://picasaweb.google.com/106826117950796832071/21030811"
        ),
        Album(
          id = "5905233023782989345",
          title = "Test2",
          description = None,
          dateTime = new Date(1374908400000L),
          coverURL = "https://lh5.googleusercontent.com/-probS2-NREE/UfOaJdp9riE/AAAAAAAATNU/gpgNcDClEFI/20130727FF22Day1.jpg",
          privacy = AlbumPrivacy.Hidden,
          link = "https://picasaweb.google.com/106826117950796832071/20130727FF22Day1"
        ),
        Album(
          id = "5871843583150354817",
          title = "Test3",
          description = Some("Desc3"),
          dateTime = new Date(1367145120000L),
          coverURL = "https://lh5.googleusercontent.com/-NT-NrpnThYY/UXz6oOZVcYE/AAAAAAAASzc/nz-2IqJmyfA/20130428PF18Day2.jpg",
          privacy = AlbumPrivacy.Public,
          link = "https://picasaweb.google.com/106826117950796832071/20130428PF18Day2" 
        )
      )
    }

  }
}

object PicasaAlbumSpec {

  val PublicAlbumXML = 
    <entry>
      <id>https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729</id>
      <published>2013-08-10T23:21:27.000Z</published>
      <updated>2013-08-10T23:34:57.689Z</updated>
      <category term="http://schemas.google.com/photos/2007#album" scheme="http://schemas.google.com/g/2005#kind"/>
      <title type="text">SomeTitle</title>
      <summary type="text"/>
      <rights type="text">public</rights>
      <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5910634723613201729" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"/>
      <link href="https://picasaweb.google.com/106826117950796832071/21030811" type="text/html" rel="alternate"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729" type="application/atom+xml" rel="self"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729/197" type="application/atom+xml" rel="edit"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729/acl" type="application/atom+xml" rel="http://schemas.google.com/acl/2007#accessControlList"/>
      <author>
        <name>Brian Hsu</name>
        <uri>https://picasaweb.google.com/106826117950796832071</uri>
      </author>
      <gphoto:id>5910634723613201729</gphoto:id>
      <gphoto:name>21030811</gphoto:name>
      <gphoto:location/>
      <gphoto:access>public</gphoto:access>
      <gphoto:timestamp>1376176887000</gphoto:timestamp>
      <gphoto:numphotos>94</gphoto:numphotos>
      <gphoto:numphotosremaining>1006</gphoto:numphotosremaining>
      <gphoto:bytesUsed>49859558</gphoto:bytesUsed>
      <gphoto:user>106826117950796832071</gphoto:user>
      <gphoto:nickname>Brian Hsu</gphoto:nickname>
      <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
      <gphoto:commentCount>0</gphoto:commentCount>
      <media:group>
        <media:content medium="image" type="image/jpeg" url="https://lh6.googleusercontent.com/-H5vGEpc8LdE/UgbK9xoN1UE/AAAAAAAATM0/9PmM99XYmN4/21030811.jpg"/>
        <media:credit>Brian Hsu</media:credit>
        <media:description type="plain"/>
        <media:keywords/>
        <media:thumbnail width="160" height="160" url="https://lh6.googleusercontent.com/-H5vGEpc8LdE/UgbK9xoN1UE/AAAAAAAATM0/9PmM99XYmN4/s160-c/21030811.jpg"/>
        <media:title type="plain">[2103-08-11] 浴衣祭</media:title>
      </media:group>
    </entry>

  val HiddenAlbumXML = 
    <entry>
      <id>https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5836947895216307969</id>
      <published>2022-02-14T08:00:00.000Z</published>
      <updated>2013-11-19T05:27:27.618Z</updated>
      <category term="http://schemas.google.com/photos/2007#album" scheme="http://schemas.google.com/g/2005#kind"/>
      <title type="text">TestTitle</title>
      <summary type="text">TestDescription</summary>
      <rights type="text">private</rights>
      <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5836947895216307969?authkey=Gv1sRgCI6bvoj4rZWJzgE" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"/>
      <link href="https://picasaweb.google.com/106826117950796832071/NewLovePlus?authkey=Gv1sRgCI6bvoj4rZWJzgE" type="text/html" rel="alternate"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5836947895216307969?authkey=Gv1sRgCI6bvoj4rZWJzgE" type="application/atom+xml" rel="self"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5836947895216307969/450?authkey=Gv1sRgCI6bvoj4rZWJzgE" type="application/atom+xml" rel="edit"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5836947895216307969/acl?authkey=Gv1sRgCI6bvoj4rZWJzgE" type="application/atom+xml" rel="http://schemas.google.com/acl/2007#accessControlList"/>
      <author>
        <name>Brian Hsu</name>
        <uri>https://picasaweb.google.com/106826117950796832071</uri>
      </author>
      <gphoto:id>5836947895216307969</gphoto:id>
      <gphoto:name>NewLovePlus</gphoto:name>
      <gphoto:location/>
      <gphoto:access>private</gphoto:access>
      <gphoto:timestamp>1644825600000</gphoto:timestamp>
      <gphoto:numphotos>439</gphoto:numphotos>
      <gphoto:numphotosremaining>661</gphoto:numphotosremaining>
      <gphoto:bytesUsed>9388398</gphoto:bytesUsed>
      <gphoto:user>106826117950796832071</gphoto:user>
      <gphoto:nickname>Brian Hsu</gphoto:nickname>
      <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
      <gphoto:commentCount>0</gphoto:commentCount>
      <media:group>
        <media:content medium="image" type="image/jpeg" url="https://lh4.googleusercontent.com/-xjDKIT-urHE/UQEBL2rU_wE/AAAAAAAATTo/51wqrF0sHj8/NewLovePlus.jpg"/>
        <media:credit>Brian Hsu</media:credit>
        <media:description type="plain"/>
        <media:keywords/>
        <media:thumbnail width="160" height="160" url="https://lh4.googleusercontent.com/-xjDKIT-urHE/UQEBL2rU_wE/AAAAAAAATTo/51wqrF0sHj8/s160-c/NewLovePlus.jpg"/>
        <media:title type="plain">[隨時更新] New LovePlus－愛花</media:title>
      </media:group>
    </entry>

  val SecretAlbumXML = 
    <entry>
      <id>https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5778560919691441265</id>
      <published>2022-12-31T08:00:00.000Z</published>
      <updated>2013-07-20T03:52:06.129Z</updated>
      <category term="http://schemas.google.com/photos/2007#album" scheme="http://schemas.google.com/g/2005#kind"/>
      <title type="text">PrivateTest</title>
      <summary type="text"/>
      <rights type="text">protected</rights>
      <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5778560919691441265" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"/>
      <link href="https://picasaweb.google.com/106826117950796832071/mxYgBC" type="text/html" rel="alternate"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5778560919691441265" type="application/atom+xml" rel="self"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5778560919691441265/255" type="application/atom+xml" rel="edit"/>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5778560919691441265/acl" type="application/atom+xml" rel="http://schemas.google.com/acl/2007#accessControlList"/>
      <author>
        <name>Brian Hsu</name>
        <uri>https://picasaweb.google.com/106826117950796832071</uri>
      </author>
      <gphoto:id>5778560919691441265</gphoto:id>
      <gphoto:name>mxYgBC</gphoto:name>
      <gphoto:location/>
      <gphoto:access>protected</gphoto:access>
      <gphoto:timestamp>1672473600000</gphoto:timestamp>
      <gphoto:numphotos>248</gphoto:numphotos>
      <gphoto:numphotosremaining>852</gphoto:numphotosremaining>
      <gphoto:bytesUsed>240816027</gphoto:bytesUsed>
      <gphoto:user>106826117950796832071</gphoto:user>
      <gphoto:nickname>Brian Hsu</gphoto:nickname>
      <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
      <gphoto:commentCount>0</gphoto:commentCount>
      <media:group>
        <media:content medium="image" type="image/jpeg" url="https://lh4.googleusercontent.com/-E9TOZCEsi54/UDGSilMptHE/AAAAAAAAS1U/zXHN7L1-S0M/mxYgBC.jpg"/>
        <media:credit>Brian Hsu</media:credit>
        <media:description type="plain"/>
        <media:keywords/>
        <media:thumbnail width="160" height="160" url="https://lh4.googleusercontent.com/-E9TOZCEsi54/UDGSilMptHE/AAAAAAAAS1U/zXHN7L1-S0M/s160-c/mxYgBC.jpg"/>
        <media:title type="plain">[隨時更新] 私人收藏</media:title>
      </media:group>
    </entry>


  val AlbumList =
    <feed>
      <entry>
        <id>https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729</id>
        <published>2013-08-10T23:21:27.000Z</published>
        <updated>2013-08-10T23:34:57.689Z</updated>
        <category term="http://schemas.google.com/photos/2007#album" scheme="http://schemas.google.com/g/2005#kind"/>
        <title type="text">Test1</title>
        <summary type="text"/>
        <rights type="text">public</rights>
        <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5910634723613201729" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"/>
        <link href="https://picasaweb.google.com/106826117950796832071/21030811" type="text/html" rel="alternate"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729" type="application/atom+xml" rel="self"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729/197" type="application/atom+xml" rel="edit"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5910634723613201729/acl" type="application/atom+xml" rel="http://schemas.google.com/acl/2007#accessControlList"/>
        <author>
          <name>Brian Hsu</name>
          <uri>https://picasaweb.google.com/106826117950796832071</uri>
        </author>
        <gphoto:id>5910634723613201729</gphoto:id>
        <gphoto:name>21030811</gphoto:name>
        <gphoto:location/>
        <gphoto:access>protected</gphoto:access>
        <gphoto:timestamp>1376176887000</gphoto:timestamp>
        <gphoto:numphotos>94</gphoto:numphotos>
        <gphoto:numphotosremaining>1006</gphoto:numphotosremaining>
        <gphoto:bytesUsed>49859558</gphoto:bytesUsed>
        <gphoto:user>106826117950796832071</gphoto:user>
        <gphoto:nickname>Brian Hsu</gphoto:nickname>
        <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
        <gphoto:commentCount>0</gphoto:commentCount>
        <media:group>
          <media:content medium="image" type="image/jpeg" url="https://lh6.googleusercontent.com/-H5vGEpc8LdE/UgbK9xoN1UE/AAAAAAAATM0/9PmM99XYmN4/21030811.jpg"/>
          <media:credit>Brian Hsu</media:credit>
          <media:description type="plain"/>
          <media:keywords/>
          <media:thumbnail width="160" height="160" url="https://lh6.googleusercontent.com/-H5vGEpc8LdE/UgbK9xoN1UE/AAAAAAAATM0/9PmM99XYmN4/s160-c/21030811.jpg"/>
          <media:title type="plain">[2103-08-11] 浴衣祭</media:title>
        </media:group>
      </entry>
      <entry>
        <id>https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5905233023782989345</id>
        <published>2013-07-27T07:00:00.000Z</published>
        <updated>2013-07-27T10:10:50.383Z</updated>
        <category term="http://schemas.google.com/photos/2007#album" scheme="http://schemas.google.com/g/2005#kind"/>
        <title type="text">Test2</title>
        <summary type="text"/>
        <rights type="text">public</rights>
        <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5905233023782989345" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"/>
        <link href="https://picasaweb.google.com/106826117950796832071/20130727FF22Day1" type="text/html" rel="alternate"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5905233023782989345" type="application/atom+xml" rel="self"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5905233023782989345/77" type="application/atom+xml" rel="edit"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5905233023782989345/acl" type="application/atom+xml" rel="http://schemas.google.com/acl/2007#accessControlList"/>
        <author>
          <name>Brian Hsu</name>
          <uri>https://picasaweb.google.com/106826117950796832071</uri>
        </author>
        <gphoto:id>5905233023782989345</gphoto:id>
        <gphoto:name>20130727FF22Day1</gphoto:name>
        <gphoto:location>台大體育館</gphoto:location>
        <gphoto:access>private</gphoto:access>
        <gphoto:timestamp>1374908400000</gphoto:timestamp>
        <gphoto:numphotos>32</gphoto:numphotos>
        <gphoto:numphotosremaining>1068</gphoto:numphotosremaining>
        <gphoto:bytesUsed>68663996</gphoto:bytesUsed>
        <gphoto:user>106826117950796832071</gphoto:user>
        <gphoto:nickname>Brian Hsu</gphoto:nickname>
        <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
        <gphoto:commentCount>0</gphoto:commentCount>
        <media:group>
          <media:content medium="image" type="image/jpeg" url="https://lh5.googleusercontent.com/-probS2-NREE/UfOaJdp9riE/AAAAAAAATNU/gpgNcDClEFI/20130727FF22Day1.jpg"/>
          <media:credit>Brian Hsu</media:credit>
          <media:description type="plain"/>
          <media:keywords/>
          <media:thumbnail width="160" height="160" url="https://lh5.googleusercontent.com/-probS2-NREE/UfOaJdp9riE/AAAAAAAATNU/gpgNcDClEFI/s160-c/20130727FF22Day1.jpg"/>
          <media:title type="plain">[2013-07-27] FF22 Day1</media:title>
        </media:group>
      </entry>
      <entry>
        <id>https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5871843583150354817</id>
        <published>2013-04-28T10:32:00.000Z</published>
        <updated>2013-06-11T03:08:11.820Z</updated>
        <category term="http://schemas.google.com/photos/2007#album" scheme="http://schemas.google.com/g/2005#kind"/>
        <title type="text">Test3</title>
        <summary type="text">Desc3</summary>
        <rights type="text">protected</rights>
        <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5871843583150354817" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"/>
        <link href="https://picasaweb.google.com/106826117950796832071/20130428PF18Day2" type="text/html" rel="alternate"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5871843583150354817" type="application/atom+xml" rel="self"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5871843583150354817/101" type="application/atom+xml" rel="edit"/>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5871843583150354817/acl" type="application/atom+xml" rel="http://schemas.google.com/acl/2007#accessControlList"/>
        <author>
          <name>Brian Hsu</name>
          <uri>https://picasaweb.google.com/106826117950796832071</uri>
        </author>
        <gphoto:id>5871843583150354817</gphoto:id>
        <gphoto:name>20130428PF18Day2</gphoto:name>
        <gphoto:location/>
        <gphoto:access>public</gphoto:access>
        <gphoto:timestamp>1367145120000</gphoto:timestamp>
        <gphoto:numphotos>91</gphoto:numphotos>
        <gphoto:numphotosremaining>1009</gphoto:numphotosremaining>
        <gphoto:bytesUsed>371872416</gphoto:bytesUsed>
        <gphoto:user>106826117950796832071</gphoto:user>
        <gphoto:nickname>Brian Hsu</gphoto:nickname>
        <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
        <gphoto:commentCount>0</gphoto:commentCount>
        <media:group>
          <media:content medium="image" type="image/jpeg" url="https://lh5.googleusercontent.com/-NT-NrpnThYY/UXz6oOZVcYE/AAAAAAAASzc/nz-2IqJmyfA/20130428PF18Day2.jpg"/>
          <media:credit>Brian Hsu</media:credit>
          <media:description type="plain"/>
          <media:keywords/>
          <media:thumbnail width="160" height="160" url="https://lh5.googleusercontent.com/-NT-NrpnThYY/UXz6oOZVcYE/AAAAAAAASzc/nz-2IqJmyfA/s160-c/20130428PF18Day2.jpg"/>
          <media:title type="plain">[2013-04-28] PF18 Day2</media:title>
        </media:group>
      </entry>
    </feed>
}
