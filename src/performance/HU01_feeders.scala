package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU01_feeders extends Simulation {

	val csvFeeder = csv("announcements.csv").circular

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map(
		"Accept" -> "image/webp,*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1")

	val headers_9 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object LoginO3 {
		val loginO3 = exec(http("LoginO3")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedO3")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "owner3")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object ListAnnouncements {
		val listAnnouncements = exec(http("ListAnnouncements")
			.get("/announcements")
			.headers(headers_0))
		.pause(10)
	}

	object FormAnnouncements {
		val formAnnouncements = exec(http("FormAnnouncements")
			.get("/announcements/new")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.feed(csvFeeder)
		.exec(http("AnnouncementCreated")
			.post("/announcements/new")
			.headers(headers_2)
			.formParam("name", "${name}")
			.formParam("petName", "${petName}")
			.formParam("description", "${description}")
			.formParam("canBeAdopted", "${canBeAdopted}")
			.formParam("type", "${type}")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	val scn = scenario("Registered").exec(Home.home,
										LoginO3.loginO3,
										ListAnnouncements.listAnnouncements,
										FormAnnouncements.formAnnouncements)

	/*
	setUp(scn1.inject(atOnceUsers(1)),
        scn2.inject(atOnceUsers(1))
        ).protocols(httpProtocol)
	*/
		
	setUp(
		scn.inject(rampUsers(1000) during (10 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(6000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95),
			forAll.failedRequests.percent.lte(0)
		)
}