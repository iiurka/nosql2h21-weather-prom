package net.weather.prometheus.metrics

import io.prometheus.client.{CollectorRegistry, Counter}

object SimpleCounter {

  val myCounter: Counter =
    Counter
      .build()
      .name("CounterName")
      .labelNames("status")
      .help("Counter desc")
      .register()
}
