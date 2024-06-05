package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class MyAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        debugLog("onServiceConnected()")
        debugServiceInfo()
        showControlButtons()
    }

    private fun showControlButtons() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val layout = ConstraintLayout(this)
        val lp = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            format = PixelFormat.TRANSLUCENT
            flags = flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.START
        }

        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.service_controls_layout, layout)

        layout.findViewById<Button>(R.id.buttonRecordGestures).setOnClickListener { view ->
            /*GestureRecordActivity.apply {
                if (isRecordingNow()) {
                    (view as Button).text = getString(R.string.start_recording_emoji)
                    stopRecording()
                }
                else {
                    (view as Button).text = getString(R.string.stop_recording_emoji)
                    GestureStorage.clear()
                    startRecording()
                }
            }*/

            swipePageUp()
        }

        layout.findViewById<Button>(R.id.buttonReplayGestures).setOnClickListener {

//            replayUserGesturesOneByOne(GestureStorage.popFirst())

            /*UserGestureSimplifier(GestureStorage.getAll()).simplifyMax().also {
                replayUserGesturesOneByOne(it)
            }*/

            swipePageDown(3000)
        }

        wm.addView(layout, lp)
    }

    private fun replayUserGesturesOneByOne(gesturePoint: GesturePoint?) {

        if (null == gesturePoint)
            return

        debugLog("userGesture: $gesturePoint")

        /*dispatchGesture(
            gesturePoint.toGestureDescription(2000),
            object: GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    GestureRecord.popFirst()?.also {
                        replayUserGesturesOneByOne(it)
                    }
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                }
            },
            null
        )*/
    }

    private fun debugServiceInfo() {
        serviceInfo?.also {
            debugLog(("serviceInfo: $it"))
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
            swipePageDown()
        }

        return with(node) {
            "className: ${className}, text: $text, desc: $contentDescription"
        }
    }

    private fun swipePageDown(duration: Long = 1000) {
        dispatchGesture(
            createUpSwipe(duration),
            null,
            null
        )
    }

    private fun swipePageUp(duration: Long = 1000) {
        dispatchGesture(createDownSwipe(duration), null, null)
    }

    private fun createUpSwipe(duration: Long): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(
                StrokeDescription(fromDownToUpPath(),0, duration)
            )
        }.build()
    }

    private fun createDownSwipe(duration: Long): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(
                StrokeDescription(fromUpToDownPath(),0, duration)
            )
        }.build()
    }

    private fun fromDownToUpPath(): android.graphics.Path {
        return android.graphics.Path().apply {
            moveTo(360f, 1000f)
            lineTo(370f, 900f)
            lineTo(340f, 800f)
            lineTo(300f, 700f)
            lineTo(250f, 550f)
            lineTo(290f, 540f)
            lineTo(320f, 300f)
            lineTo(300f, 210f)
            lineTo(360f, 150f)
        }
    }

    private fun fromUpToDownPath(): android.graphics.Path {
        return android.graphics.Path().apply {
            moveTo(350f, 140f)
            lineTo(370f, 1010f)
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