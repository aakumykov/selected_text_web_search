package com.github.aakumykov.ktor_server

import android.content.Intent
import android.os.Bundle
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
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ServerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServerBinding
    private val ktorServiceIntent: Intent by lazy { Intent(this, KtorService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareLayout()
        startKtorService()

        binding.startServiceButton.setOnClickListener { startKtorService() }
        binding.stopServiceButton.setOnClickListener { stopKtorService() }
        binding.connectToWebsocketServerButton.setOnClickListener { connectToWebsocketServer() }
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
            client.wss(
                method = HttpMethod.Get,
                host = "192.168.0.171",
                port = 8080,
                path = "/chat"
            ) {

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
}

