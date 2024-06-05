package com.github.aakumykov.kotlin_playground

object GestureStorage {

    private val deque: ArrayDeque<UserGesture> = ArrayDeque(5)

    fun addIfNotNull(userGesture: UserGesture?) {
        userGesture?.also { deque.add(it) }
    }

    fun clear() = deque.clear()

    fun popFirst(): UserGesture? {
        return deque.removeFirstOrNull()
    }
}