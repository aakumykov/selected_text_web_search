package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class GestureRecordActivity : AppCompatActivity(), View.OnTouchListener {

    private var startingEvent: MotionEvent? = null
    private var currentRecord: GestureRecord = GestureRecord()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_record)

        findViewById<View>(R.id.main).setOnTouchListener(this)

        openAccessibilitySettingsIfDisabled()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.d(TAG, "motionEvent: $event")
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> startingEvent = event
            MotionEvent.ACTION_MOVE -> recordEvent(event)
            MotionEvent.ACTION_UP -> finishRecording(event)
            else -> {}
        }
        return true
    }

    private fun finishRecording(event: MotionEvent) {
        recordEvent(event)
        GestureRecordsStorage.addRecord(currentRecord)
        startingEvent = null
    }

    private fun recordEvent(event: MotionEvent) {
        /*if (null == startingEvent)
            startingEvent = event
        else*/
            currentRecord.addIfNotNull(GesturePoint.fromMotionEvent(startingEvent, event))
    }

    companion object {
        val TAG: String = GestureRecordActivity::class.java.simpleName

        private var recordingIsActive: Boolean = false

        fun startRecording() { recordingIsActive = true }

        fun stopRecording() { recordingIsActive = false }

        fun isRecordingNow() = recordingIsActive
    }
}
