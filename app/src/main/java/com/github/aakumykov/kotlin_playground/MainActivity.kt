package com.github.aakumykov.kotlin_playground

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import com.github.aakumykov.kotlin_playground.databinding.ActivityMainBinding
import com.github.aakumykov.kotlin_playground.extensions.LogD
import com.github.aakumykov.kotlin_playground.extensions.showToast
import com.github.aakumykov.kotlin_playground.extensions.tag


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.messages.observe(this) { messages -> binding.logView.text = messages.joinToString("\n") }

        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate()")

        LogD("onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appInfoButton.setOnClickListener { showAppProperties() }
        binding.clearLogButton.setOnClickListener { Logger.clear() }

        binding.button1.setOnClickListener {action1() }
        binding.button2.setOnClickListener { action2() }
        binding.button3.setOnClickListener { action3() }
        binding.button4.setOnClickListener { action4() }


        if (!isAccessibilityServiceEnabled())
            openAccessibilitySettings()
    }

    override fun onResume() {
        super.onResume()
        printASIsEnabled()
    }

    private fun printASIsEnabled() {
        Logger.d(tag(), "Служба доступности включена: ${isAccessibilityServiceEnabled()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(TAG, "onDestroy()")
    }

    private fun action1() {
        openAccessibilitySettings()
    }

    private fun action2() {
        printASIsEnabled()
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
