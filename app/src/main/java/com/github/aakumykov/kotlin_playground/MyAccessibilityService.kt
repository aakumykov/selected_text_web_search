package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        debugLog("onServiceConnected()")
        serviceInfo?.also {
            debugLog(("serviceInfo: $it"))
            it.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE
        }
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.also { e ->
            when(e.eventType) {
                AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> debugLog("TYPE_GESTURE_DETECTION_START")
                AccessibilityEvent.TYPE_GESTURE_DETECTION_END ->  debugLog("TYPE_GESTURE_DETECTION_END")
                AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START ->  debugLog("TYPE_TOUCH_EXPLORATION_GESTURE_START")
                AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> debugLog("TYPE_TOUCH_EXPLORATION_GESTURE_END")
                AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> debugLog("TYPE_TOUCH_INTERACTION_START")
                AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> debugLog("TYPE_TOUCH_INTERACTION_END")
                else -> {
//                    debugLog(e.eventType.toString())
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onGesture(gestureId: Int): Boolean {
        debugLog("onGesture(${gestureId})")
        GestureStorage.addGesture("gestureId: $gestureId")
        return super.onGesture(gestureId)
    }

    override fun onGesture(gestureEvent: AccessibilityGestureEvent): Boolean {
        debugLog("onGesture(${gestureEvent.motionEvents.size})")
        GestureStorage.addGesture("gestureEvent: ${gestureEvent.gestureId}")
        return super.onGesture(gestureEvent)
    }

    private fun debugLog(text: String) {
        Logger.d(TAG, text)
    }

    companion object {
        val TAG: String = MyAccessibilityService::class.java.simpleName
    }
}
