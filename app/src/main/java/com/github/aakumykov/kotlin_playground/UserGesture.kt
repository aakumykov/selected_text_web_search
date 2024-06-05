package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.Path
import android.view.MotionEvent

data class UserGesture(
    val isInitial: Boolean,
    val fromX: Float?,
    val fromY: Float?,
    val toX: Float,
    val toY: Float,
    val strokeDelay: Long,
    val duration: Long,
    val endingEventTime: Long
) {
    private fun toPath(): Path {
        return Path().apply {
            if (isInitial)
                moveTo(fromX!!, fromY!!)
            lineTo(toX, toY)
        }
    }

    private fun toStrokeDescription(): StrokeDescription {
        return StrokeDescription(
            toPath(),
            strokeDelay,
            duration
        )
    }

    fun toGestureDescription(): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(toStrokeDescription())
        }.build()
    }

    override fun toString(): String {
        return UserGesture::class.simpleName + " { " +
                (if (isInitial) "(initial) " else "") +
                "x: $fromX -> $toX, y: $fromY -> $toY, dur: $duration " +
                "}"
    }

    companion object {

        fun fromScrollEvent(e1: MotionEvent?, e2: MotionEvent): UserGesture? {
            return if (null != e1) {
                UserGesture(
                    isInitial = true,
                    fromX = e1.rawX,
                    fromY = e1.rawY,
                    toX = e2.rawX,
                    toY = e2.rawY,
                    strokeDelay = 0L,
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
                isInitial = false,
                fromX = prev.toX,
                fromY = prev.toY,
                toX = e2.rawX,
                toY = e2.rawY,
                strokeDelay = 0L,
                duration = e2.eventTime - prev.endingEventTime,
                endingEventTime = e2.eventTime
            )
        }
    }
}
