package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MyAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        debugLog("onServiceConnected()")
        serviceInfo?.also {
            debugLog(("serviceInfo: $it"))
            /*it.packageNames?.joinToString { "," }.also {
                debugLog(("packageNames: $it"))
            }*/
        }
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        event?.also { e ->
            when(e.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> debugWindowEvent(e)
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> debugWindowContentEvent(e)
                else -> {}
            }
        }

        /*rootInActiveWindow?.also { rootView ->
            debugLog("--------------")
            debugLog("rootView.packageName: ${rootView.packageName}, childCount: ${rootView.childCount}")
            showChildrenRecursively(0, rootView.getChildren())
        }*/
    }

    private fun debugWindowContentEvent(event: AccessibilityEvent) {

    }

    private fun debugWindowEvent(event: AccessibilityEvent) {
        debugLog("event: $event")
    }

    fun isBrowserWebView(node: AccessibilityNodeInfo?, text: String): Boolean {
        return node?.let {
            "android.webkit.WebView" == it.className
                    /*&& (it.text?.let { t -> !t.equals(text) } ?: false)*/
        } ?: false
    }

    private fun showChildrenRecursively(level: Int, list: List<AccessibilityNodeInfo>) {
        list.forEachIndexed { i, node ->

            showNodeInfo(level, i, node)

            node?.getChildren()?.also { childList: List<AccessibilityNodeInfo> ->
                if (childList.isNotEmpty())
                    showChildrenRecursively(level+1, childList)
            }
        }
    }

    private fun showNodeInfo(level: Int, index: Int, node: AccessibilityNodeInfo) {
        node?.apply {
            val indent = "━━".repeat(level)
            debugLog("┝$indent i=$index, ${simpleNodeInfo(node)}")
        }
    }

    private fun simpleNodeInfo(node: AccessibilityNodeInfo): String {

        if (isBrowserWebView(node, WEB_PAGE_TITLE)) {
            debugLog("Найден WebView с '${WEB_PAGE_TITLE}'")
//            scrollDownOnNode(node)
            makeSwipeDownGesture()
        }

        return with(node) {
            "className: ${className}, text: $text, desc: $contentDescription"
        }
    }

    private fun makeSwipeDownGesture() {
        debugLog("makeSwipeDownGesture()")

        dispatchGesture(
            swipeFromUpToDownGesture(),
            object: GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    debugLog("onCompleted()")
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    debugLog("onCancelled()")
                }
            },
            null
        )
    }

    private fun swipeFromUpToDownGesture(): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(StrokeDescription(swipeDownPath(),0, 1000))
        }.build()
    }

    private fun swipeDownPath(): android.graphics.Path {
        return android.graphics.Path().apply {
            moveTo(360f, 1000f)
            lineTo(360f, 150f)
        }
    }

    private fun scrollDownOnNode(node: AccessibilityNodeInfo?) {
        node?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    }

    private fun debugLog(text: String) {
        Logger.d(TAG, text)
    }

    companion object {
        val TAG: String = MyAccessibilityService::class.java.simpleName
        const val ACTION_SCROLL_DOWN: String = "ACTION_SCROLL_DOWN"
        const val ACTION_SCROLL_UP: String = "ACTION_SCROLL_UP"
        const val WEB_PAGE_TITLE: String = "Проза.ру"
    }
}

fun AccessibilityNodeInfo.getChildren(): List<AccessibilityNodeInfo> {
    return when(childCount) {
        0 -> emptyList()
        else -> MutableList(childCount) { getChild(it) }
    }
}