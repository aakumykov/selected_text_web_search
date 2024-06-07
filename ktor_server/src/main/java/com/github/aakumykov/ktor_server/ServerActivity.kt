package com.github.aakumykov.ktor_server

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Carriers.PORT
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.aakumykov.ktor_server.databinding.ActivityServerBinding
import com.gitlab.aakumykov.exception_utils_module.ExceptionUtils
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

class ServerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServerBinding

    private val ktorServiceIntent: Intent by lazy {
        Intent(this, KtorService::class.java).apply {
            putExtra(EXTRAS_SERVER_IP_ADDRESS, serverIpAddress)
            putExtra(PORT, serverPort)
        }
    }

    private val serverIpAddress: String get() {
        return serverAddressParts.let { addressParts ->
            return@let if (addressParts?.size == 2) addressParts.first()
            else DEFAULT_SERVER_IP
        }
    }

    private val serverPort: Int get() {
        return serverAddressParts.let { addressParts ->
            return@let if (addressParts?.size == 2) addressParts.last().toInt()
            else DEFAULT_SERVER_PORT
        }
    }

    private val serverAddressParts: List<String>? get() = binding.serverAddressInput.text?.split(":")


    private lateinit var notificationsPermissionRequest: PermissionsRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preparePermissionsRequest()
        prepareLayout()
        displayIpAddress()

        binding.startServiceButton.setOnClickListener { startKtorServiceWithNotificationsPermission() }
        binding.stopServiceButton.setOnClickListener { stopKtorService() }
        binding.connectToWebsocketServerButton.setOnClickListener { connectToWebsocketServer() }
    }

    private fun startKtorServiceWithNotificationsPermission() {
        if (isAndroid33OrLater())
            notificationsPermissionRequest.launch()
        else
            startKtorService()
    }


    private fun isAndroid33OrLater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU


    private fun preparePermissionsRequest() {
        if (isAndroid33OrLater()) {
            notificationsPermissionRequest = constructPermissionsRequest(
                android.Manifest.permission.POST_NOTIFICATIONS,
                requiresPermission = ::startKtorService
            )
        }
    }

    private fun displayIpAddress() {
        binding.serverAddressInput.setText("${localIpAddress}:${DEFAULT_SERVER_PORT}")
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

            inMainThread { hideError() }

            try {

                client.webSocket(
                    method = HttpMethod.Get,
                    host = serverIpAddress,
                    port = serverPort,
                    path = "/chat"
                ) {
                    Log.d(TAG, "Слиент соединился с сервером. Сессия: $this")

//                isActive

                    try {
                        incoming.receive()
                            .also { frame ->
                                (frame as? Frame.Text)?.also { textFrame ->
                                    Log.d(TAG, "Получен ответ: " + textFrame.readText())
                                }
                            }
                    } catch (e: ClosedReceiveChannelException) {
                        Log.e(TAG, ExceptionUtils.getErrorMessage(e))
                    } catch (e: CancellationException) {
                        Log.e(TAG, ExceptionUtils.getErrorMessage(e))
                    }

                    send("Привет!")

                    incoming.receive().also { frame ->
                        (frame as? Frame.Text)?.also { textFrame ->
                            Log.d(TAG, "Получен ответ: " + textFrame.readText())
                        }
                    }

                    delay(10000)
                    Log.d(TAG, "Слиент отключается от сервера.")
                    close()
                }

            } catch (e: Exception) {
                inMainThread { showError(e) }
            }
        }
    }

    private suspend fun inMainThread(block: () -> Unit) {
        withContext(Dispatchers.Main) { block.invoke() }
    }

    private fun showError(e: Exception) {
        binding.errorView.apply {
            text = ExceptionUtils.getErrorMessage(e)
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        binding.errorView.apply {
            text = ""
            visibility = View.GONE
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

