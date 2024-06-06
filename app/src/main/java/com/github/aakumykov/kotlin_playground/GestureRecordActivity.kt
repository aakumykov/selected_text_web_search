package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GestureRecordActivity : AppCompatActivity(), View.OnTouchListener {

    private var initialEvent: MotionEvent? = null
    private var pointList: MutableList<GesturePoint> = ArrayList()
    private var startingTime: Long? = null
    private var endingTime: Long? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_record)

        findViewById<View>(R.id.main).setOnTouchListener(this)

        openAccessibilitySettingsIfDisabled()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        Log.d(TAG, "motionEvent: $event")
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> startRecording(event)
            MotionEvent.ACTION_MOVE -> recordEvent(event)
            MotionEvent.ACTION_UP -> finishRecording(event)
            MotionEvent.ACTION_CANCEL -> cancelRecording()
            MotionEvent.ACTION_OUTSIDE -> { Toast.makeText(this, "ACTION_OUTSIDE", Toast.LENGTH_SHORT).show() }
            else -> {}
        }
        return true
    }

    private fun startRecording(event: MotionEvent) {
        Log.d(TAG, "startRecording(), $event")
        eraseRecordingData()
        startingTime = event.eventTime
        initialEvent = event
    }

    private fun recordEvent(event: MotionEvent) {
        Log.d(TAG, "recordEvent(), $event")
        storeMotionEvent(event)
    }

    private fun finishRecording(event: MotionEvent) {
        Log.d(TAG, "finishRecording(), $event")

        storeMotionEvent(event)
        endingTime = event.eventTime

        if (null != startingTime)
            GestureRecordsStorage.addRecordIfNotNull(
                GestureRecord.create(pointList, startingTime!!, endingTime!!)
            )

        eraseRecordingData()
    }

    private fun cancelRecording() {
        eraseRecordingData()
    }

    private fun storeMotionEvent(event: MotionEvent) {
        initialEvent?.also {
            pointList.add(GesturePoint.fromMotionEvent(it, event))
        }
    }

    private fun eraseRecordingData() {
        startingTime = null
        endingTime = null
        initialEvent = null
        pointList.clear()
    }

    companion object {
        val TAG: String = GestureRecordActivity::class.java.simpleName

        private var recordingIsActive: Boolean = false

        fun activateRecordingState() { recordingIsActive = true }

        fun deactivateRecordingState() { recordingIsActive = false }

        fun isRecordingState() = recordingIsActive
    }
}
