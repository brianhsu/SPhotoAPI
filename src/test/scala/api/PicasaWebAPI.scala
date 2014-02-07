package org.bone.sphotoapi.api

import org.scalatest.FunSpec
import org.scalatest.Matchers

import org.bone.sphotoapi.oauth._

import org.scribe.model.Verb

import scala.util.Success
import scala.util.Failure

object PicasaWebAPIMock extends PicasaWebOAuth(null, null, null, null) with MockOAuth {
  
  override def sendRequest_(url: String, verb: Verb, 
                            getParams: Map[String, String], 
                            postParams: Map[String, String],
                            headers: Map[String, String],
                            payload: Option[Array[Byte]]) = 
  {
    (url, verb) match {
      case ("user/1234", Verb.GET) => Success((200, "text/xml", albumsXML.toString))
      case ("user/default/albumid/5678?imgmax=d&thumbsize=200u,320u,640u,720u,1024u", Verb.GET) => Success((200, "text/xml", photosXML.toString))
      case ("https://www.googleapis.com/oauth2/v1/userinfo", Verb.GET) => Success(200, "json", userInfo)
      case _ => Success((404, "text/html", "NotFound"))
    }
  }

  val userInfo = """{
    "id": "12345678",
    "email": "test@example.com",
    "verified_email": true
  }"""

  val albumsXML = 
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

  val photosXML = 
    <feed xmlns:gphoto="http://schemas.google.com/photos/2007">
      <entry>
        <id> https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049938998052398770 </id>
        <published>2007-04-05T13:42:27.000Z</published>
        <updated>2013-11-18T04:12:04.426Z</updated>
        <category term="http://schemas.google.com/photos/2007#photo" scheme="http://schemas.google.com/g/2005#kind"> </category>
        <title type="text">rene.jpg</title>
        <summary type="text"/>
        <content src="https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/rene.jpg" type="image/jpeg"> </content>
        <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049938998052398770" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"> </link>
        <link href="https://picasaweb.google.com/106826117950796832071/NownoF#5049938998052398770" type="text/html" rel="alternate"> </link>
        <link href="https://picasaweb.google.com/lh/photo/B9MuKcxI_UxrRvi7oRtvpdMTjNZETYmyPJy0liipFm0" type="text/html" rel="http://schemas.google.com/photos/2007#canonical"> </link>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049938998052398770" type="application/atom+xml" rel="self"> </link>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049938998052398770/1215880203" type="application/atom+xml" rel="edit"> </link>
        <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049938998052398770/1215880203" type="image/jpeg" rel="edit-media"> </link>
        <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049938998052398770/1215880203" type="image/jpeg" rel="media-edit"> </link>
        <link href="https://picasaweb.google.com/lh/reportAbuse?uname=106826117950796832071&amp;aid=5049936206323655841&amp;iid=5049938998052398770" type="text/html" rel="http://schemas.google.com/photos/2007#report"> </link>
        <gphoto:id>5049938998052398770</gphoto:id>
        <gphoto:version>1215880203</gphoto:version>
        <gphoto:position>16.17391</gphoto:position>
        <gphoto:albumid>5049936206323655841</gphoto:albumid>
        <gphoto:access>public</gphoto:access>
        <gphoto:width>800</gphoto:width>
        <gphoto:height>600</gphoto:height>
        <gphoto:size>381072</gphoto:size>
        <gphoto:client/>
        <gphoto:checksum>0</gphoto:checksum>
        <gphoto:timestamp>1175780547000</gphoto:timestamp>
        <gphoto:imageVersion>1903</gphoto:imageVersion>
        <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
        <gphoto:commentCount>0</gphoto:commentCount>
        <gphoto:license url="" name="保留所有權利" id="0"> ALL_RIGHTS_RESERVED </gphoto:license>
        <gphoto:shapes faces="done"/>
        <exif:tags>
          <exif:imageUniqueID> 000000000000000000000000000000000000000000000000000000003deabbe10000000000000000000000000000000000000000000000000000000080b319be </exif:imageUniqueID>
        </exif:tags>
        <media:group>
          <media:content medium="image" type="image/jpeg" width="512" height="384" url="https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/rene.jpg"> </media:content>
          <media:credit>Brian Hsu</media:credit>
          <media:description type="plain"/>
          <media:keywords/>
          <media:thumbnail width="72" height="54" url="https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/s72/rene.jpg"> </media:thumbnail>
          <media:thumbnail width="144" height="108" url="https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/s144/rene.jpg"> </media:thumbnail>
          <media:thumbnail width="288" height="216" url="https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/s288/rene.jpg"> </media:thumbnail>
          <media:title type="plain">rene.jpg</media:title>
        </media:group>
      </entry>
      <entry>
        <id> https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049939625117624050 </id>
        <published>2007-04-05T13:44:53.000Z</published>
        <updated>2013-11-18T04:12:04.426Z</updated>
        <category term="http://schemas.google.com/photos/2007#photo" scheme="http://schemas.google.com/g/2005#kind"> </category>
        <title type="text">southDisk.jpg</title>
        <summary type="text"/>
        <content src="https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/southDisk.jpg" type="image/jpeg"> </content>
        <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049939625117624050" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"> </link>
        <link href="https://picasaweb.google.com/106826117950796832071/NownoF#5049939625117624050" type="text/html" rel="alternate"> </link>
        <link href="https://picasaweb.google.com/lh/photo/KaSQsQ_ePJsxzyy-pDni69MTjNZETYmyPJy0liipFm0" type="text/html" rel="http://schemas.google.com/photos/2007#canonical"> </link>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049939625117624050" type="application/atom+xml" rel="self"> </link>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049939625117624050/1215880204" type="application/atom+xml" rel="edit"> </link>
        <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049939625117624050/1215880204" type="image/jpeg" rel="edit-media"> </link>
        <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5049939625117624050/1215880204" type="image/jpeg" rel="media-edit"> </link>
        <link href="https://picasaweb.google.com/lh/reportAbuse?uname=106826117950796832071&amp;aid=5049936206323655841&amp;iid=5049939625117624050" type="text/html" rel="http://schemas.google.com/photos/2007#report"> </link>
        <gphoto:id>5049939625117624050</gphoto:id>
        <gphoto:version>1215880204</gphoto:version>
        <gphoto:position>17.043476</gphoto:position>
        <gphoto:albumid>5049936206323655841</gphoto:albumid>
        <gphoto:access>public</gphoto:access>
        <gphoto:width>1600</gphoto:width>
        <gphoto:height>1200</gphoto:height>
        <gphoto:size>1204434</gphoto:size>
        <gphoto:client/>
        <gphoto:checksum>0</gphoto:checksum>
        <gphoto:timestamp>1175780692000</gphoto:timestamp>
        <gphoto:imageVersion>1912</gphoto:imageVersion>
        <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
        <gphoto:commentCount>0</gphoto:commentCount>
        <gphoto:license url="" name="保留所有權利" id="0"> ALL_RIGHTS_RESERVED </gphoto:license>
        <gphoto:shapes faces="done"/>
        <exif:tags>
          <exif:imageUniqueID> 000000000000000000000000000000000000000000000000000000001e9dc68300000000000000000000000000000000000000000000000000000000b19c9ba7 </exif:imageUniqueID>
        </exif:tags>
        <media:group>
          <media:content medium="image" type="image/jpeg" width="512" height="384" url="https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/southDisk.jpg"> </media:content>
          <media:credit>Brian Hsu</media:credit>
          <media:description type="plain"/>
          <media:keywords/>
          <media:thumbnail width="72" height="54" url="https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/s72/southDisk.jpg"> </media:thumbnail>
          <media:thumbnail width="144" height="108" url="https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/s144/southDisk.jpg"> </media:thumbnail>
          <media:thumbnail width="288" height="216" url="https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/s288/southDisk.jpg"> </media:thumbnail>
          <media:title type="plain">southDisk.jpg</media:title>
        </media:group>
      </entry>
      <entry>
        <id> https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5253168305703161186 </id>
        <published>2008-10-04T05:35:57.000Z</published>
        <updated>2013-11-18T04:12:04.426Z</updated>
        <category term="http://schemas.google.com/photos/2007#photo" scheme="http://schemas.google.com/g/2005#kind"> </category>
        <title type="text">GPL3.jpg</title>
        <summary type="text"/>
        <content src="https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/GPL3.jpg" type="image/jpeg"> </content>
        <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5253168305703161186" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"> </link>
        <link href="https://picasaweb.google.com/106826117950796832071/NownoF#5253168305703161186" type="text/html" rel="alternate"> </link>
        <link href="https://picasaweb.google.com/lh/photo/p0DNe5_ktkQOg2Cwxm1IVtMTjNZETYmyPJy0liipFm0" type="text/html" rel="http://schemas.google.com/photos/2007#canonical"> </link>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5253168305703161186" type="application/atom+xml" rel="self"> </link>
        <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5253168305703161186/4" type="application/atom+xml" rel="edit"> </link>
        <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5253168305703161186/4" type="image/jpeg" rel="edit-media"> </link>
        <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5253168305703161186/4" type="image/jpeg" rel="media-edit"> </link>
        <link href="https://picasaweb.google.com/lh/reportAbuse?uname=106826117950796832071&amp;aid=5049936206323655841&amp;iid=5253168305703161186" type="text/html" rel="http://schemas.google.com/photos/2007#report"> </link>
        <gphoto:id>5253168305703161186</gphoto:id>
        <gphoto:version>4</gphoto:version>
        <gphoto:position>17.913042</gphoto:position>
        <gphoto:albumid>5049936206323655841</gphoto:albumid>
        <gphoto:access>public</gphoto:access>
        <gphoto:width>345</gphoto:width>
        <gphoto:height>182</gphoto:height>
        <gphoto:size>44973</gphoto:size>
        <gphoto:client/>
        <gphoto:checksum/>
        <gphoto:timestamp>1223098557000</gphoto:timestamp>
        <gphoto:imageVersion>4955</gphoto:imageVersion>
        <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
        <gphoto:commentCount>0</gphoto:commentCount>
        <gphoto:license url="" name="保留所有權利" id="0"> ALL_RIGHTS_RESERVED </gphoto:license>
        <gphoto:shapes faces="done"/>
        <exif:tags>
          <exif:imageUniqueID>a9694c40e589f3ff77e4e34af2d38f63</exif:imageUniqueID>
        </exif:tags>
        <media:group>
          <media:content medium="image" type="image/jpeg" width="345" height="182" url="https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/GPL3.jpg"> </media:content>
          <media:credit>Brian Hsu</media:credit>
          <media:description type="plain"/>
          <media:keywords/>
          <media:thumbnail width="72" height="38" url="https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/s72/GPL3.jpg"> </media:thumbnail>
          <media:thumbnail width="144" height="76" url="https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/s144/GPL3.jpg"> </media:thumbnail>
          <media:thumbnail width="288" height="152" url="https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/s288/GPL3.jpg"> </media:thumbnail>
          <media:title type="plain">GPL3.jpg</media:title>
        </media:group>
      </entry>
    </feed>

}

class PicasaWebAPISpec extends FunSpec with Matchers {

  val api = PicasaWebAPI.withMock(PicasaWebAPIMock)

  describe("PicasaWebAPI") {

    it("get exist user's album list correctly") {
      api.getAlbums("1234").get.map(_.id) shouldBe List("5910634723613201729", "5905233023782989345", "5871843583150354817")
    }

    it("get exist albums's photo list correctly") {
      api.getPhotos("5678").get.map(_.id) shouldBe List("5049938998052398770", "5049939625117624050", "5253168305703161186")
    }

    it("return correct information about user") {
      api.getUserInfo.get shouldBe ("12345678", "test@example.com")
    }

    it("return Failure when user not found") {
      api.getAlbums("userNotFound").isFailure shouldBe true
    }

    it("return Failure when album not found") {
      api.getAlbums("photoNotFound").isFailure shouldBe true
    }

  }

}

