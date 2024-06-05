package com.github.aakumykov.kotlin_playground

import android.view.MotionEvent

data class UserGesture(
    val fromX: Float,
    val fromY: Float,
    val toX: Float,
    val toY: Float,
    val startDelay: Long,
    val duration: Long
) {
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
