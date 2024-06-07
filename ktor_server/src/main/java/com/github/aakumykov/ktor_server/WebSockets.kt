package com.github.aakumykov.ktor_server

import android.util.Log
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedSendChannelException
import java.time.Duration
import kotlin.coroutines.cancellation.CancellationException

fun Application.configureWebsockets(loggingTag: String, qwerty: String) {

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/chat") {

            qwerty.toString()

            try {
                outgoing.send(Frame.Text("Вы подключились!"))
            } catch (e: ClosedSendChannelException) {
                Log.e(loggingTag, ExceptionUtils.getErrorMessage(e))
            } catch (e: CancellationException) {
                Log.e(loggingTag, ExceptionUtils.getErrorMessage(e))
            }

            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                send("Вы написали: $receivedText")
            }
        }
    }
}