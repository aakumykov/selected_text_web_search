package com.github.aakumykov.kotlin_playground

import android.util.Log
import com.github.aakumykov.kotlin_playground.extensions.tag

object GestureStorage {

    init {
        Log.d(tag(), "init")
    }

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

    fun getAll(): List<UserGesture> = deque.toList()

    fun popAll(): List<UserGesture> {
        return deque.toList().also {
            clear()
        }
    }
}