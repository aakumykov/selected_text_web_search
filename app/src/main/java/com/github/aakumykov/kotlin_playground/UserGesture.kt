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
    val duration: Long,
    val endingEventTime: Long
) {
    private fun toPath(): Path {
        return Path().apply {
            moveTo(fromX, fromY)
            lineTo(toX, toY)
        }
    }

    private fun toStrokeDescription(): StrokeDescription {
        return StrokeDescription(
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
                    duration = e2.eventTime - e2.downTime,
                    endingEventTime = e2.eventTime
                )
            }
            else {
                null
            }
        }

        fun fromPreviousEvent(prev: UserGesture?, e2: MotionEvent): UserGesture? {
            if (null == prev) return null
            return UserGesture(
                fromX = prev.toX,
                fromY = prev.toY,
                toX = e2.rawX,
                toY = e2.rawY,
                startDelay = 0L,
                duration = e2.eventTime - prev.endingEventTime,
                endingEventTime = e2.eventTime
            )
        }
    }
}
