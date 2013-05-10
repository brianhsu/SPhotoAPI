package org.bone.sphotoapi.oauth

import org.scalatest.FunSpec
import org.scalatest.PrivateMethodTester 
import org.scalatest.matchers.ShouldMatchers

import org.scribe.model.OAuthRequest
import org.scribe.model.Parameter
import org.scribe.model.Verb

import java.util.Date
import scala.xml.Node
import net.liftweb.json.JsonAST._

class ImgUrOAuthSpec extends FunSpec with ShouldMatchers with PrivateMethodTester {

  describe("ImgUrOAuth") {

    val imgUrOAuth = new ImgUrOAuth(
      imgUrAPIPrefix = "https://api.imgur.com/", 
      service = null, 
      appKey = null, 
      appSecret = null,
      accessToken = None,
      refreshToken = None,
      expireAt = new Date
    )

    it("build GET request with parameter correctly") {

      val buildRequest = PrivateMethod[OAuthRequest]('buildRequest)

      val request = imgUrOAuth invokePrivate buildRequest (
        "http://localhost/get", Verb.GET,
        List(
          "option1" -> "HelloWorld",
          "option2" -> "Foo",
          "option3" -> "Bar"
        )
      )

      request.getUrl should be === "http://localhost/get"
      request.getVerb should be === Verb.GET
      request.getBodyParams.size should be === 0

      val buildParams = request.getQueryStringParams
      buildParams.size should be === 3
      buildParams.contains(new Parameter("option1", "HelloWorld")) should be === true
      buildParams.contains(new Parameter("option2", "Foo")) should be === true
      buildParams.contains(new Parameter("option3", "Bar")) should be === true
    }

    it("build POST request with parameter correctly") {

      val buildRequest = PrivateMethod[OAuthRequest]('buildRequest)

      val request = imgUrOAuth invokePrivate buildRequest (
        "http://localhost/post", Verb.POST,
        List(
          "option1" -> "HelloWorld",
          "option2" -> "Foo",
          "option3" -> "Bar",
          "option4" -> "FooBar"
        )
      )

      request.getUrl should be === "http://localhost/post"
      request.getVerb should be === Verb.POST
      request.getQueryStringParams.size should be === 0

      val buildParams = request.getBodyParams
      buildParams.size should be === 4
      buildParams.contains(new Parameter("option1", "HelloWorld")) should be === true
      buildParams.contains(new Parameter("option2", "Foo")) should be === true
      buildParams.contains(new Parameter("option3", "Bar")) should be === true
      buildParams.contains(new Parameter("option4", "FooBar")) should be === true
    }

    it("parse well-formed normal XML correctly") {

      val parseXML = PrivateMethod[Node]('parseXML)

      val xmlNode = imgUrOAuth invokePrivate parseXML ("""
        <data success="1" status="200">
          <id>384077</id>
          <reputation>158</reputation>
        </data>
      """)

      xmlNode should be === 
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

      status should be === 200
      success should be === true
      id.toInt should be === 384077
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

      exception.rawResponse should be === rawContent

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

      exception.rawResponse should be === rawContent

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

