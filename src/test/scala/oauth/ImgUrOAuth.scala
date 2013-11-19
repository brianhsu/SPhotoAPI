package org.bone.sphotoapi.oauth

import org.scalatest.FunSpec
import org.scalatest.PrivateMethodTester 
import org.scalatest.Matchers

import java.util.Date
import scala.xml.Node
import net.liftweb.json.JsonAST._

class ImgUrOAuthSpec extends FunSpec with Matchers with PrivateMethodTester {

  describe("ImgUrOAuth") {

    val imgUrOAuth = new ImgUrOAuth(
      service = null, 
      appKey = null, 
      appSecret = null,
      accessToken = None,
      refreshToken = None,
      expireAt = new Date
    )

    it("parse well-formed normal XML correctly") {

      val parseXML = PrivateMethod[Node]('parseXML)

      val xmlNode = imgUrOAuth invokePrivate parseXML ("""
        <data success="1" status="200">
          <id>384077</id>
          <reputation>158</reputation>
        </data>
      """)

      xmlNode shouldBe 
        <data success="1" status="200">
          <id>384077</id>
          <reputation>158</reputation>
        </data>
    }

    it("parse well-formed normal JSON correctly") {

      val parseJSON = PrivateMethod[JValue]('parseJSON)
      val json = imgUrOAuth invokePrivate parseJSON("""
        {
            "data" : {
                "id"         : 384077,
            },
            "status"  : 200,
            "success" : true
        }
      """)

      val JInt(status) = json \ "status"
      val JBool(success) = json \ "success"
      val JInt(id) = json \\ "id"

      status shouldBe 200
      success shouldBe true
      id.toInt shouldBe 384077
    }

    it("parse well-formed error XML correctly") {
      val parseXML = PrivateMethod[Node]('parseXML)
      val rawContent = """
        <data>
          <error>
              <message>This method requires authentication</message>
              <request>/3/account</request>
              <method>get</method>
              <format>xml</format>
              <parameters/>
            </error>
        </data>
      """

      val exception = intercept[OAuthException] {
        imgUrOAuth invokePrivate parseXML (rawContent)
      }

      exception.rawResponse shouldBe rawContent

    }

    it("parse well-formed error JSON correctly") {

      val parseJSON = PrivateMethod[JValue]('parseJSON)
      val rawContent = """
        {
            "data": {
                "error": {
                    "message": "This method requires authentication",
                    "request": "\/3\/account.json",
                    "method": "get",
                    "format": "json",
                    "parameters": ""
                }
            },
            "success": false,
            "status": 403
        }
      """

      val exception = intercept[OAuthException] {
        imgUrOAuth invokePrivate parseJSON (rawContent)
      }

      exception.rawResponse shouldBe rawContent

    }

    it("throw exception when not well-formed XML") {

      val parseXML = PrivateMethod[Node]('parseXML)
      val rawContent = """
        <data><error><error></data>
      """

      intercept[Exception] {
        imgUrOAuth invokePrivate parseXML (rawContent)
      }
    }

    it("throw exception when not well-formed JSON") {

      val parseJSON = PrivateMethod[JValue]('parseJSON)
      val rawContent = """qqqq: qqqq"""

      intercept[Exception] {
        imgUrOAuth invokePrivate parseJSON (rawContent)
      }
    }

  }
}

