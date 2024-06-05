package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MyAccessibilityService : AccessibilityService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            when(action) {
                ACTION_SCROLL_DOWN -> scrollDown()
                ACTION_SCROLL_UP -> scrollUp()
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun scrollUp() {

    }

    private fun scrollDown() {

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        debugLog("onServiceConnected()")
        serviceInfo?.also {
            debugLog(("serviceInfo: $it"))
        }
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        val rootView = rootInActiveWindow

        debugLog("--------------")
        debugLog("rootView.packageName: ${rootView.packageName}, childCount: ${rootView.childCount}")

        rootView.apply {
            for (i in 0..<childCount) {
                val child = getChild(i)
                val className = child.className
                val text = child.text
                val desc = child.contentDescription
                debugLog("┝━━ i=$i, className: $className, text: $text, desc: $desc")
            }
        }

    }

    private fun debugLog(text: String) {
        Logger.d(TAG, text)
    }

    companion object {
        val TAG: String = MyAccessibilityService::class.java.simpleName
        const val ACTION_SCROLL_DOWN: String = "ACTION_SCROLL_DOWN"
        const val ACTION_SCROLL_UP: String = "ACTION_SCROLL_UP"
    }
}
