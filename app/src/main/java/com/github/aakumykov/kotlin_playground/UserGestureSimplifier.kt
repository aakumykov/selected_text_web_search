package com.github.aakumykov.kotlin_playground

class UserGestureSimplifier(private val gestureList: List<GesturePoint>) {

    /**
     * Прореживает список, оставляя "пропуски" длиной в [gapSize].
     * Не трогает начальное и конечное значения.
     */
    /*fun simplify(gapSize: Int = 2): List<UserGesture> {

    }*/

    fun simplifyMax(): GesturePoint? {
        return when(gestureList.size) {
            0 -> null
            1 -> gestureList.first()
            else -> simplifyReal()
        }
    }

    private fun simplifyReal(): GesturePoint {

        val fist = gestureList.first()
        val last = gestureList.last()

        return GesturePoint(
            fromX = fist.fromX,
            fromY = fist.fromY,
            toX = last.toX,
            toY = last.toY,
        )
    }
}