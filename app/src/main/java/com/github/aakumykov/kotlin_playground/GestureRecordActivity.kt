package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class GestureRecordActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private lateinit var mDetector: GestureDetectorCompat

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_record)

        mDetector = GestureDetectorCompat(this, this)
        mDetector.setOnDoubleTapListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d(TAG, "onDown: $event")
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Log.d(TAG, "onLongPress: $event")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(TAG, "onFling: $e1 $e2")
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Log.d(TAG, "onShowPress: $event")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.d(TAG, "onSingleTapUp: $event")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(TAG, "onScroll: $e1 $e2")
        return true
    }

    override fun onDoubleTap(event: MotionEvent): Boolean {
        Log.d(TAG, "onDoubleTap: $event")
        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onDoubleTapEvent: $event")
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        Log.d(TAG, "onSingleTapConfirmed: $event")
        return true
    }

    companion object {
        val TAG: String = GestureRecordActivity::class.java.simpleName
    }
}
