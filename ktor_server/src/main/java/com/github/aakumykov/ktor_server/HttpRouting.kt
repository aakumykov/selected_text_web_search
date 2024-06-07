package com.github.aakumykov.ktor_server

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureHttpRouting() {

    routing {
        get("/") {
            call.respondText("Привет от Ktor! Время $dateTime")
        }
    }
}
