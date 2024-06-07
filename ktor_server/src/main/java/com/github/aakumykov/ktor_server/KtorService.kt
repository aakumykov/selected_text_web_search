package com.github.aakumykov.ktor_server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stopServerOnCancellation
import io.ktor.server.jetty.Jetty
import io.ktor.server.jetty.JettyApplicationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val EXTRAS_SERVER_IP_ADDRESS = "SERVER_IP_ADDRESS"
const val EXTRAS_SERVER_PORT = "SERVER_PORT"

class KtorService : Service() {

    private var runningServer: JettyApplicationEngine? = null
    private var inputIntent: Intent? = null
    private val serverAddress get() = inputIntent?.getStringExtra(EXTRAS_SERVER_IP_ADDRESS) ?: DEFAULT_SERVER_IP
    private val serverPort get() = inputIntent?.getIntExtra(EXTRAS_SERVER_PORT, DEFAULT_SERVER_PORT) ?: DEFAULT_SERVER_PORT


    private val mNotificationsBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
    }


    override fun onBind(intent: Intent): IBinder {
        return Binder(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onCreate() {
        super.onCreate()

        prepareNotificationChannel()
        showDutyNotification()
        startKtorServer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        inputIntent = intent
        return START_NOT_STICKY
    }

    private fun showDutyNotification() {
        showPersistentNotification(
            getString(R.string.KTOR_SERER_NOTIFICATION_title),
            getString(R.string.KTOR_SERER_NOTIFICATION_message_running),
            R.drawable.ic_websocket
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        runningServer?.stop(500,1000)
    }

    private fun prepareNotificationChannel() {
        NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel())
    }


    private fun notificationChannel(): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
            .setName(getString(R.string.KTOR_SERER_SERVICE_notification_channel_name))
            .build()
    }


    private fun showPersistentNotification(
        title: String,
        message: String,
        @DrawableRes iconRes: Int
    ) {
        startForeground(NOTIFICATION_ID, prepareNotification(title, message, iconRes).build())
    }


    private fun prepareNotification(
        title: String,
        message: String,
        @DrawableRes iconRes: Int
    ): NotificationCompat.Builder {
        return mNotificationsBuilder
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
    }

    fun shutdown() {
        stopSelf()
    }


    private fun startKtorServer() {
        CoroutineScope(Dispatchers.IO).launch {

            runningServer = embeddedServer(
                Jetty,
                host = serverAddress,
                port = serverPort,
            ) {
                configureWebsockets(KTOR_WEBSOCKET_SERVER_TAG, ":-)")
                configureHttpRouting()
            }.start(wait = true) // TODO: попробовать wait = false

        }
    }


    companion object {
        val TAG: String = KtorService::class.java.simpleName
        val CHANNEL_ID: String = "${TAG}_notification_channel_id"
        val NOTIFICATION_ID: Int = R.id.ktor_server_notification
    }


    class Binder(val service: KtorService): android.os.Binder()
}

const val KTOR_WEBSOCKET_SERVER_TAG: String = "KTOR_SERVER"