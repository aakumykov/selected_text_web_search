package com.github.aakumykov.ktor_server

import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.aakumykov.ktor_server.databinding.ActivityServerBinding
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServerActivity : AppCompatActivity(), ServiceConnection {

    private lateinit var binding: ActivityServerBinding
    private val ktorServiceIntent: Intent by lazy { Intent(this, KtorService::class.java) }
    private var ktorService: KtorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareLayout()
        binding.startServiceButton.setOnClickListener { startKtorService() }
        binding.stopServiceButton.setOnClickListener { stopKtorService() }
        startKtorService()
    }

    private fun startKtorService() {
        startService(ktorServiceIntent)
        bindService(ktorServiceIntent, this, BIND_AUTO_CREATE)
    }

    private fun stopKtorService() {
        stopService(ktorServiceIntent)
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        ktorService = (binder as? KtorService.Binder)?.service
        showToast("Служба подключена")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        ktorService = null
        showToast("Служба отключена")
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

