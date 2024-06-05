package com.github.aakumykov.kotlin_playground

class UserGestureSimplifier(private val gestureList: List<UserGesture>) {

    /**
     * Прореживает список, оставляя "пропуски" длиной в [gapSize].
     * Не трогает начальное и конечное значения.
     */
    /*fun simplify(gapSize: Int = 2): List<UserGesture> {

    }*/

    fun simplifyMax(): UserGesture? {
        return when(gestureList.size) {
            0 -> null
            1 -> gestureList.first()
            else -> simplifyReal()
        }
    }

    private fun simplifyReal(): UserGesture {

        val fist = gestureList.first()
        val last = gestureList.last()

        return UserGesture(
            fromX = fist.fromX,
            fromY = fist.fromY,
            toX = last.toX,
            toY = last.toY,
            startDelay = 0L,
            duration = gestureList.map { it.duration }.reduce { acc, l -> l+acc; acc },
            endingEventTime = last.endingEventTime
        )
    }
}