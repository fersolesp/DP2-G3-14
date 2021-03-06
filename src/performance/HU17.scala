package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU17 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map("User-Agent" -> "Rainmeter WebParser plugin")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(10)
	}

	object LoginOwner4 {
        val loginOwner4 = exec(http("LoginOwner4")
            .get("/login")
            .headers(headers_0)
            .check(css("input[name=_csrf]","value").saveAs("stoken"))
        ).pause(10)
        .exec(http("LoggedOwner4")
            .post("/login")
            .headers(headers_2)
            .formParam("username", "owner4")
            .formParam("password", "0wn3r")
            .formParam("_csrf", "${stoken}"))
        .pause(10)
	}
	
	object LoginOwner6 {
        val loginOwner6 = exec(http("LoginOwner6")
            .get("/login")
            .headers(headers_0)
            .check(css("input[name=_csrf]","value").saveAs("stoken"))
        ).pause(10)
        .exec(http("LoggedOwner6")
            .post("/login")
            .headers(headers_2)
            .formParam("username", "owner6")
            .formParam("password", "0wn3r")
            .formParam("_csrf", "${stoken}"))
        .pause(10)
    }

	object ShowCourses {
		val showCourses = exec(http("ShowCourses")
			.get("/courses")
			.headers(headers_0))
		.pause(10)
	}
	
	object ShowCourse  {
		val showCourse = exec(http("ShowCourse")
			.get("/courses/1")
			.headers(headers_0))
		.pause(10)
	}

	object NewInscription {
		val newInscription = exec(http("NewInscription")
			.get("/courses/1/inscription/new")
			.headers(headers_2)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(10)

		.exec(http("SavedInscription")
			.post("/courses/1/inscription/new")
			.headers(headers_2)
			.formParam("date", "2020/05/19")
			.formParam("pet", "Iggy")
			.formParam("isPaid", "false")
			.formParam("_csrf", "${stoken}") 
	)
		.pause(10)
	}

	object NewInscription6 {
		val newInscription6 = exec(http("NewInscription6")
			.get("/courses/1/inscription/new")
			.headers(headers_2)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(10)

		.exec(http("SavedInscription6")
			.post("/courses/1/inscription/new")
			.headers(headers_2)
			.formParam("date", "2020/05/19")
			.formParam("pet", "Samantha")
			.formParam("isPaid", "false")
			.formParam("_csrf", "${stoken}") 
	)
		.pause(10)
	}

	val scn1 = scenario("scn1").exec(Home.home,
									  LoginOwner4.loginOwner4,
									  ShowCourses.showCourses,
									  ShowCourse.showCourse,
									  NewInscription.newInscription)
									  
	val scn2 = scenario("scn2").exec(Home.home,
									  LoginOwner6.loginOwner6,
									  ShowCourses.showCourses,
									  ShowCourse.showCourse,
									  NewInscription6.newInscription6)


	setUp(scn1.inject(rampUsers(5415) during (100 seconds)),
			scn2.inject(rampUsers(5415) during (100 seconds))
			).protocols(httpProtocol)
			 .assertions(
				 global.responseTime.max.lt(6000),
				 global.responseTime.mean.lt(1000),
				 global.successfulRequests.percent.gt(95)
			 )
}