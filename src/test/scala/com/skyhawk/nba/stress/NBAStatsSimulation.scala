package com.skyhawk.nba.stress

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class NBAStatsSimulation extends Simulation {

  // HTTP Configuration
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .userAgentHeader("Gatling/Stress Test")

  // Scenario Definitions
  val playerStatsScenario = scenario("Player Statistics")
    .exec(
      http("Get Player Statistics")
        .get("/api/statistics/player/1")
        .queryParam("startDate", "2024-03-01T00:00:00")
        .queryParam("endDate", "2024-03-31T23:59:59")
        .check(status.is(200))
    )
    .pause(1)

  val teamStatsScenario = scenario("Team Statistics")
    .exec(
      http("Get Team Statistics")
        .get("/api/statistics/team/1")
        .queryParam("startDate", "2024-03-01T00:00:00")
        .queryParam("endDate", "2024-03-31T23:59:59")
        .check(status.is(200))
    )
    .pause(1)

  val gameStatsScenario = scenario("Game Statistics")
    .exec(
      http("Get Game Statistics")
        .get("/api/statistics/game/1")
        .check(status.is(200))
    )
    .pause(1)

  val gameEventsScenario = scenario("Game Events")
    .exec(
      http("Get Game Events")
        .get("/api/statistics/events")
        .queryParam("gameId", "1")
        .queryParam("eventType", "SHOT_MADE")
        .check(status.is(200))
    )
    .pause(1)

  val leaderboardScenario = scenario("Leaderboard")
    .exec(
      http("Get Leaderboard")
        .get("/api/statistics/leaderboard")
        .queryParam("statType", "points")
        .queryParam("startDate", "2024-03-01T00:00:00")
        .queryParam("endDate", "2024-03-31T23:59:59")
        .queryParam("limit", "10")
        .check(status.is(200))
    )
    .pause(1)

  // Load Simulation
  setUp(
    playerStatsScenario.inject(
      rampUsers(50).during(10.seconds),
      constantUsersPerSec(5).during(1.minute)
    ),
    teamStatsScenario.inject(
      rampUsers(50).during(10.seconds),
      constantUsersPerSec(5).during(1.minute)
    ),
    gameStatsScenario.inject(
      rampUsers(50).during(10.seconds),
      constantUsersPerSec(5).during(1.minute)
    ),
    gameEventsScenario.inject(
      rampUsers(50).during(10.seconds),
      constantUsersPerSec(5).during(1.minute)
    ),
    leaderboardScenario.inject(
      rampUsers(50).during(10.seconds),
      constantUsersPerSec(5).during(1.minute)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.max.lt(2000),
      global.successfulRequests.percent.gt(95)
    )
} 