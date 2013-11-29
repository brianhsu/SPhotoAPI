package org.bone.sphotoapi.parser

import org.bone.sphotoapi.model.Photo
import org.bone.sphotoapi.model.Thumbnail

import org.scalatest.FunSpec
import org.scalatest.Matchers

import java.util.Date

class PicasaWebPhotoSpec extends FunSpec with Matchers {

  import PicasaWebPhotoSpec._

  describe("PicasaWebPhotoSpec") {
    it ("should parse photo with title XML correctly") {
      PicasaWebPhoto(photoWithTitle) shouldBe Photo(
        id = "5277210049635511490",
        title = Some("SomeTitle"),
        timestamp = new Date(1228696212000L),
        mimeType = "image/jpeg",
        width = 704,
        height = 396,
        link = "https://picasaweb.google.com/106826117950796832071/NownoF#5277210049635511490",
        imageURL = "https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/1225635920875.jpg",
        thumbnails = List(
          Thumbnail("https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/s72/1225635920875.jpg", 72),
          Thumbnail("https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/s144/1225635920875.jpg", 144),
          Thumbnail("https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/s288/1225635920875.jpg", 288)
        )
      )
    }

    it ("should parse photo list XML correctly") {
      PicasaWebPhoto.fromXML(photoList) shouldBe List(
        Photo(
          id = "5049938998052398770",
          title = None,
          timestamp = new Date(1175780547000L),
          mimeType = "image/jpeg",
          width = 800,
          height = 600,
          link = "https://picasaweb.google.com/106826117950796832071/NownoF#5049938998052398770",
          imageURL = "https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/rene.jpg",
          thumbnails = List(
            Thumbnail("https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/s72/rene.jpg", 72),
            Thumbnail("https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/s144/rene.jpg", 144),
            Thumbnail("https://lh6.googleusercontent.com/-9IhwY3SQM0A/RhT8w1ROJrI/AAAAAAAAB28/qmeBrG0mHec/s288/rene.jpg", 288)
          )
        ),
        Photo(
          id = "5049939625117624050",
          title = None,
          timestamp = new Date(1175780692000L),
          mimeType = "image/jpeg",
          width = 1600,
          height = 1200,
          link = "https://picasaweb.google.com/106826117950796832071/NownoF#5049939625117624050",
          imageURL = "https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/southDisk.jpg",
          thumbnails = List(
            Thumbnail("https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/s72/southDisk.jpg", 72),
            Thumbnail("https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/s144/southDisk.jpg", 144),
            Thumbnail("https://lh4.googleusercontent.com/-RH0qG7I5qYs/RhT9VVROJvI/AAAAAAAAB3g/7Qv1rhJvClA/s288/southDisk.jpg", 288)
          )
        ),
        Photo(
          id = "5253168305703161186",
          title = None,
          timestamp = new Date(1223098557000L),
          mimeType = "image/jpeg",
          width = 345,
          height = 182,
          link = "https://picasaweb.google.com/106826117950796832071/NownoF#5253168305703161186",
          imageURL = "https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/GPL3.jpg",
          thumbnails = List(
            Thumbnail("https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/s72/GPL3.jpg", 72),
            Thumbnail("https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/s144/GPL3.jpg", 144),
            Thumbnail("https://lh4.googleusercontent.com/-f4pyPQVDGwk/SOcAvdbHDWI/AAAAAAAAE1s/XVKP_naksxs/s288/GPL3.jpg", 288) 

          )
        )
      )
    }
  }
}

object PicasaWebPhotoSpec extends FunSpec with Matchers {
  val photoWithTitle = 
    <entry xmlns:gphoto="http://schemas.google.com/photos/2007">
      <id> https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5277210049635511490 </id>
      <published>2008-12-08T00:30:12.000Z</published>
      <updated>2013-11-18T04:12:04.426Z</updated>
      <category term="http://schemas.google.com/photos/2007#photo" scheme="http://schemas.google.com/g/2005#kind"> </category>
      <title type="text">1225635920875.jpg</title>
      <summary type="text">SomeTitle</summary>
      <content src="https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/1225635920875.jpg" type="image/jpeg"> </content>
      <link href="https://picasaweb.google.com/data/feed/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5277210049635511490" type="application/atom+xml" rel="http://schemas.google.com/g/2005#feed"> </link>
      <link href="https://picasaweb.google.com/106826117950796832071/NownoF#5277210049635511490" type="text/html" rel="alternate"> </link>
      <link href="https://picasaweb.google.com/lh/photo/JhK2f9phKUjnemLKkWUx3tMTjNZETYmyPJy0liipFm0" type="text/html" rel="http://schemas.google.com/photos/2007#canonical"> </link>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5277210049635511490" type="application/atom+xml" rel="self"> </link>
      <link href="https://picasaweb.google.com/data/entry/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5277210049635511490/7" type="application/atom+xml" rel="edit"> </link>
      <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5277210049635511490/7" type="image/jpeg" rel="edit-media"> </link>
      <link href="https://picasaweb.google.com/data/media/api/user/106826117950796832071/albumid/5049936206323655841/photoid/5277210049635511490/7" type="image/jpeg" rel="media-edit"> </link>
      <link href="https://picasaweb.google.com/lh/reportAbuse?uname=106826117950796832071&amp;aid=5049936206323655841&amp;iid=5277210049635511490" type="text/html" rel="http://schemas.google.com/photos/2007#report"> </link>
      <gphoto:id>5277210049635511490</gphoto:id>
      <gphoto:version>7</gphoto:version>
      <gphoto:position>19.652174</gphoto:position>
      <gphoto:albumid>5049936206323655841</gphoto:albumid>
      <gphoto:access>public</gphoto:access>
      <gphoto:width>704</gphoto:width>
      <gphoto:height>396</gphoto:height>
      <gphoto:size>36028</gphoto:size>
      <gphoto:client/>
      <gphoto:checksum/>
      <gphoto:timestamp>1228696212000</gphoto:timestamp>
      <gphoto:imageVersion>6582</gphoto:imageVersion>
      <gphoto:commentingEnabled>true</gphoto:commentingEnabled>
      <gphoto:commentCount>0</gphoto:commentCount>
      <gphoto:license url="" name="保留所有權利" id="0"> ALL_RIGHTS_RESERVED </gphoto:license>
      <gphoto:shapes faces="done"/>
      <exif:tags>
        <exif:imageUniqueID>9489acdb2d7635bfce04b24d49ee7d35</exif:imageUniqueID>
      </exif:tags>
      <media:group>
        <media:content medium="image" type="image/jpeg" width="512" height="288" url="https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/1225635920875.jpg"> </media:content>
        <media:credit>Brian Hsu</media:credit>
        <media:description type="plain">SomeTitle</media:description>
        <media:keywords/>
        <media:thumbnail width="72" height="41" url="https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/s72/1225635920875.jpg"> </media:thumbnail>
        <media:thumbnail width="144" height="81" url="https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/s144/1225635920875.jpg"> </media:thumbnail>
        <media:thumbnail width="288" height="162" url="https://lh6.googleusercontent.com/-n-I-1ugzOkw/STxqlI2lbMI/AAAAAAAAGbY/HB7_al2uLto/s288/1225635920875.jpg"> </media:thumbnail>
        <media:title type="plain">1225635920875.jpg</media:title>
      </media:group>
    </entry>


  val photoList = 
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
