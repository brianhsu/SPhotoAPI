package org.bone.sphotoapi.oauth

import org.scalatest.FunSpec
import org.scalatest.PrivateMethodTester 
import org.scalatest.Matchers

import org.scribe.model.OAuthRequest
import org.scribe.model.Parameter
import org.scribe.model.Verb

class OAuthSpec extends FunSpec with Matchers with PrivateMethodTester {

  describe("OAuth") {
    val imgUrOAuth = new OAuth {
      val prefixURL = "http://example/api/"
    }

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
