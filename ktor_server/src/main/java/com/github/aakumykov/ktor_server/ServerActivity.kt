package com.github.aakumykov.ktor_server

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.aakumykov.ktor_server.databinding.ActivityServerBinding
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ServerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServerBinding
    private val ktorServiceIntent: Intent by lazy { Intent(this, KtorService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareLayout()
        displayIpAddress()
        startKtorService()

        binding.startServiceButton.setOnClickListener { startKtorService() }
        binding.stopServiceButton.setOnClickListener { stopKtorService() }
        binding.connectToWebsocketServerButton.setOnClickListener { connectToWebsocketServer() }
    }

    private fun displayIpAddress() {
        binding.ipAddressView.text = localIpAddress
    }

    private fun connectToWebsocketServer() {

        val client = HttpClient(OkHttp) {
            engine {
                preconfigured = OkHttpClient.Builder()
                    .pingInterval(20, TimeUnit.SECONDS)
                    .build()
            }
            install(WebSockets)
        }

        lifecycleScope.launch(Dispatchers.IO){
            client.webSocket(
                method = HttpMethod.Get,
                host = localIpAddress,
                port = serverPort,
                path = "/chat"
            ) {
                Log.d(TAG, "Слиент соединился с сервером. Сессия: $this")

                incoming.receive().also { frame ->
                    (frame as? Frame.Text)?.also { textFrame ->
                        Log.d(TAG, "Получен ответ: "+textFrame.readText())
                    }
                }

                send("Привет!")

                incoming.receive().also { frame ->
                    (frame as? Frame.Text)?.also { textFrame ->
                        Log.d(TAG, "Получен ответ: "+textFrame.readText())
                    }
                }

                delay(10000)
                Log.d(TAG, "Слиент отключается от сервера.")
                close()
            }
        }
    }

    private fun startKtorService() {
        startService(ktorServiceIntent)
    }

    private fun stopKtorService() {
        stopService(ktorServiceIntent)
    }

    private fun prepareLayout() {
        binding = ActivityServerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG: String = ServerActivity::class.java.simpleName
    }
}

