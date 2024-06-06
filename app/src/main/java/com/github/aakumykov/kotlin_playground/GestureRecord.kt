package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.GestureDescription
import android.graphics.Path

class GestureRecord {

    private var _pointList: MutableList<GesturePoint> = ArrayList()

    fun addIfNotNull(gesturePoint: GesturePoint?) {
        gesturePoint?.also { _pointList.add(it) }
    }

    fun createGestureDescription(duration: Long = 1000): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(createStrokeDescription(duration))
        }.build()
    }

    private fun createPath(): Path {
        return Path().apply {
            _pointList.firstOrNull()?.also {
                moveTo(it.fromX, it.fromY)
            }
            if (_pointList.size>1) {
                _pointList.subList(1, _pointList.lastIndex).also { sublist ->
                    sublist.forEach { gp ->
                        lineTo(gp.toX, gp.toY)
                    }
            }}
        }
    }

    private fun createStrokeDescription(duration: Long): GestureDescription.StrokeDescription {
        return GestureDescription.StrokeDescription(
            createPath(),
            0L,
            duration
        )
    }
}