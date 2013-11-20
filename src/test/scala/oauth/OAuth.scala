package org.bone.sphotoapi.oauth

import org.scalatest.FunSpec
import org.scalatest.PrivateMethodTester 
import org.scalatest.Matchers

import org.scribe.model.OAuthRequest
import org.scribe.model.Parameter
import org.scribe.model.Verb
import org.scribe.model.Token

import java.util.Date

class OAuthSpec extends FunSpec with Matchers with PrivateMethodTester {

  describe("OAuth") {
    val mockOAuth = new OAuth {
      def appKey: String = ""
      def appSecret: String = ""

      def prefixURL: String = ""
      def refreshURL: String = ""

      var accessToken: Option[Token] = None
      var refreshToken: Option[String] = None
      var expireAt: Date = new Date
      val service = null
    }

    it("build GET request with parameter correctly") {

      val request = mockOAuth.buildRequest (
        "http://localhost/get", Verb.GET,
         "option1" -> "HelloWorld",
         "option2" -> "Foo",
         "option3" -> "Bar"
      )

      request.getUrl shouldBe "http://localhost/get"
      request.getVerb shouldBe Verb.GET
      request.getBodyParams.size shouldBe 0

      val buildParams = request.getQueryStringParams
      buildParams.size shouldBe 3
      buildParams.contains(new Parameter("option1", "HelloWorld")) shouldBe true
      buildParams.contains(new Parameter("option2", "Foo")) shouldBe true
      buildParams.contains(new Parameter("option3", "Bar")) shouldBe true
    }

    it("build POST request with parameter correctly") {

      val request = mockOAuth.buildRequest (
        "http://localhost/post", Verb.POST,
        "option1" -> "HelloWorld",
        "option2" -> "Foo",
        "option3" -> "Bar",
        "option4" -> "FooBar"
      )

      request.getUrl shouldBe "http://localhost/post"
      request.getVerb shouldBe Verb.POST
      request.getQueryStringParams.size shouldBe 0

      val buildParams = request.getBodyParams
      buildParams.size shouldBe 4
      buildParams.contains(new Parameter("option1", "HelloWorld")) shouldBe true
      buildParams.contains(new Parameter("option2", "Foo")) shouldBe true
      buildParams.contains(new Parameter("option3", "Bar")) shouldBe true
      buildParams.contains(new Parameter("option4", "FooBar")) shouldBe true
    }
  }
}
