package io.github.hugoltsp.stonks.presenter.http

import io.ktor.application.call
import io.ktor.http.ContentType.Text.Plain
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class HealthCheckHttpEndpoint {

    fun start() {
        embeddedServer(Netty, 8080) {
            routing {
                get("/") {
                    call.respondText("Ok!", Plain)
                }
            }
        }.start(wait = true)
    }

}