package com.github.aakumykov.ktor_server

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Application.configureHttpRouting() {

    routing {
        get("/") {
            call.respondText("Привет от Ktor! Время ${dateTime()}")
        }
    }
}

fun dateTime(): String {
    return SimpleDateFormat("hhч mmм ssс, dd.MM.yy", Locale.getDefault()).format(Date())
}