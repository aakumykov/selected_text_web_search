package com.github.aakumykov.ktor_server

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send

fun Application.configureWebsockets() {
    install(WebSockets) {
//        pingPeriod = Duration.ofSeconds(15)
//        timeout = Duration.ofSeconds(15)
//        maxFrameSize = Long.MAX_VALUE
//        masking = false
    }

    routing {
        webSocket("/chat") {
            send("Вы подключились!")
            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                send("Вы написали: $receivedText")
            }
        }
    }
}