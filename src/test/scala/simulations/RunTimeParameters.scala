package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RunTimeParameters extends Simulation {

  //1 Http config

  val httpConfig = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  def getAllVideoGames() = {
      exec(http("Get all video games - 1st call")
        .get("videogames")
        .check(status.is(200)))
  }

  //2 Scenario defition

  val scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
    .pause(3)

   //3 Load Scenario
  setUp(
    scn.inject(
      nothingFor(3),
      rampUsers(1) during(1)
    )).protocols(httpConfig.inferHtmlResources())

}