package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.aakumykov.kotlin_playground.databinding.ActivityGestureRecordBinding

class GestureRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGestureRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        perpareLayout()

        binding.main.setOnTouchListener { v, motionEvent ->

            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> debugMotionEvent("ACTION_DOWN", motionEvent)
                MotionEvent.ACTION_POINTER_DOWN -> debugMotionEvent("ACTION_POINTER_DOWN", motionEvent)

                MotionEvent.ACTION_UP -> debugMotionEvent("ACTION_UP", motionEvent)
                MotionEvent.ACTION_POINTER_UP -> debugMotionEvent("ACTION_POINTER_UP", motionEvent)

                MotionEvent.ACTION_MOVE -> debugMotionEvent("ACTION_MOVE", motionEvent)
            }

            true
        }
    }

    private fun debugMotionEvent(action: String, e: MotionEvent) {
        val downTime = e.downTime
        val eventTime = e.eventTime
        val timeDiff = eventTime - downTime
        Log.d(TAG,
            "$action, x: ${e.rawX}, y: ${e.rawY}, eventTime: ${eventTime}, downTime: ${downTime}, diff: $timeDiff"
        )
    }

    private fun perpareLayout() {
        binding = ActivityGestureRecordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        val TAG: String = GestureRecordActivity::class.java.simpleName
    }
}