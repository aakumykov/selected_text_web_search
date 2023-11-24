package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.aakumykov.kotlin_playground.databinding.ActivityMainBinding
import com.github.aakumykov.kotlin_playground.extensions.showToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.messages.observe(this) { messages -> binding.logView.text = messages.joinToString("\n") }

        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clearLogButton.setOnClickListener { Logger.clear() }

        binding.button.setOnClickListener { action1() }
        binding.button2.setOnClickListener { action2() }
        binding.button3.setOnClickListener { action3() }
        binding.button4.setOnClickListener { action4() }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(TAG, "onDestroy()")
    }

    private fun action1() {
        showToast("Привет :-)")
    }

    private fun action2() {
        showToast("Привет 2")
    }

    private fun action3() {
        showToast("Привет 3")
    }

    private fun action4() {
        showToast("Привет 4")
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}