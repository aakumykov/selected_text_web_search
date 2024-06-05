package com.github.aakumykov.kotlin_playground

object GestureStorage {

    private val deque: ArrayDeque<UserGesture> = ArrayDeque(5)

    fun addIfNotNull(userGesture: UserGesture?) {
        userGesture?.also { deque.add(it) }
    }

    fun clear() = deque.clear()

    fun getLast(): UserGesture? = deque.lastOrNull()

    fun popFirst(): UserGesture? {
        return deque.removeFirstOrNull()
    }

    fun isEmpty(): Boolean = (0 == deque.size)
}