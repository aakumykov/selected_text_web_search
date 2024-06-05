package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.os.bundleOf

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
        event?.also { e ->

            debugLog("eventType: ${e.eventType}, " +
                    "action: ${e.action}, " +
                    "windowId: ${e.windowId}, " +
                    "packageName: ${e.packageName}, " +
                    "windowChanges: ${e.windowChanges}")

            e.source?.apply {
                debugLog("packageName: ${packageName}")
                performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD, bundleOf())
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
