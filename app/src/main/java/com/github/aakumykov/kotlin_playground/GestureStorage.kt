package com.github.aakumykov.kotlin_playground

object GestureStorage {

    private val deque: ArrayDeque<UserGesture> = ArrayDeque(5)

    fun addIfNotNull(userGesture: UserGesture?) {
        userGesture?.also { deque.add(it) }
    }

    fun getUserGesture(): UserGesture? = deque.firstOrNull()

    fun clear() = deque.clear()

    fun count(): Int = deque.size
}