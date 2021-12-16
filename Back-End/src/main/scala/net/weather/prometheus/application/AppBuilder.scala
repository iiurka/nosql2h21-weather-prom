package net.weather.prometheus.application

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation._
import akka.http.scaladsl.server.Route
import net.weather.prometheus.actor.WeatherActor
import net.weather.prometheus.collector.PromCollector
import net.weather.prometheus.http.{CounterRoute, PrometheusRoute}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContextExecutor, Future}


class AppBuilder(c: AppConfig) extends WeatherActor {
  val httpHost: String = c.httpEndpoint.host
  val httpPort: Int = c.httpEndpoint.port
  val routes: Route = CounterRoute.route ~ PrometheusRoute.route
  val awaitTime: FiniteDuration = c.awaitTime
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  private val collector = new PromCollector

  def initialize(): Future[Unit] = {
    Http()
      .newServerAt(httpHost, httpPort)
      .bind(routes)
    collector.collect()
    Future.unit
  }
}
