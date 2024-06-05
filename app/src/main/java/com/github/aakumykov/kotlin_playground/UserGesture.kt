package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.Path
import android.view.MotionEvent

data class UserGesture(
    val fromX: Float,
    val fromY: Float,
    val toX: Float,
    val toY: Float,
    val startDelay: Long,
    val duration: Long
) {
    fun toPath(): android.graphics.Path {
        return Path().apply {
            moveTo(fromX, fromY)
            lineTo(toX, toY)
        }
    }

    fun toStrokeDescription(): StrokeDescription {
        return GestureDescription.StrokeDescription(
            toPath(),
            startDelay,
            duration
        )
    }

    fun toGestureDescription(): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(toStrokeDescription())
        }.build()
    }


    companion object {

        fun fromScrollEvent(e1: MotionEvent?, e2: MotionEvent): UserGesture? {
            return if (null != e1) {
                UserGesture(
                    fromX = e1.rawX,
                    fromY = e1.rawY,
                    toX = e2.rawX,
                    toY = e2.rawY,
                    startDelay = 0L,
                    duration = e2.eventTime - e2.downTime
                )
            }
            else {
                null
            }
        }
    }
}
