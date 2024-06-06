package com.github.aakumykov.kotlin_playground

import android.accessibilityservice.GestureDescription
import android.graphics.Path

class GestureRecord private constructor(
    private val pointList: List<GesturePoint>,
    private val startingTime: Long,
    private val endingTime: Long
) {
    fun createGestureDescription(): GestureDescription {
        return GestureDescription.Builder().apply {
            addStroke(createStrokeDescription())
        }.build()
    }

    private fun createPath(): Path {
        return Path().apply {
            pointList.firstOrNull()?.also {
                moveTo(it.fromX, it.fromY)
            }
            if (pointList.size>1) {
                pointList.subList(1, pointList.lastIndex).also { sublist ->
                    sublist.forEach { gp ->
                        lineTo(gp.toX, gp.toY)
                    }
            }}
        }
    }

    private fun createStrokeDescription(): GestureDescription.StrokeDescription {
        return GestureDescription.StrokeDescription(
            createPath(),
            0L,
            endingTime - startingTime
        )
    }

    companion object {
        fun create(pointList: List<GesturePoint>, startingTime: Long, endingTime: Long): GestureRecord {
            return GestureRecord(pointList, startingTime, endingTime)
        }
    }
}