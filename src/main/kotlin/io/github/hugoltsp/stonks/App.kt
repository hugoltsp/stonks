package io.github.hugoltsp.stonks

import io.github.hugoltsp.stonks.infra.config.DatabaseConfig
import io.github.hugoltsp.stonks.presenter.http.HealthCheckHttpEndpoint
import io.github.hugoltsp.stonks.presenter.telegram.bot.StonksBot

fun main() {

    DatabaseConfig.connect()
    StonksBot().run()
    HealthCheckHttpEndpoint().start()

}