package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CodeReuseWithObjects extends Simulation {

  //1 Http config
  val httpConfig = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  //2 Scenario defition

  def getAllVideoGames() = {
    repeat(3) {
      exec(http("Get all video games - 1st call")
        .get("videogames")
        .check(status.is(200)))
    }
  }

  def getSpecificGame() = {
    repeat(3) {
      exec(http("Get specific game")
        .get("videogames/1")
        .check(status.is(200)))
    }
  }

  val scn = scenario("Video Game DB")
    .exec(getAllVideoGames()
      .pause(1))

    .exec(getSpecificGame()
      .pause(1))

    .exec(getAllVideoGames()
      .pause(1))

    .exec { session => println(session("responseBody").as[String]); session }

  //3 Load Scenario
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConfig)

}