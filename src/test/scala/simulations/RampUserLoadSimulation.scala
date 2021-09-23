package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RampUserLoadSimulation extends Simulation {

  //1 Http config

  val httpConfig = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  def getAllVideoGames() = {
      exec(http("Get all video games - 1st call")
        .get("videogames")
        .check(status.is(200)))
  }

  def getSpecificGame() = {
      exec(http("Get specific game")
        .get("videogames/2")
        .check(status.is(200)))
  }

  //2 Scenario defition

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
    .pause(3)
    .exec(getSpecificGame())
    .pause(3)
    .exec(getAllVideoGames())

   //3 Load Scenario
  setUp(
    scn.inject(
      nothingFor(3),
//      constantUsersPerSec(10) during(10),
      rampUsersPerSec(1) to (5) during(20)
    )).protocols(httpConfig.inferHtmlResources())

}