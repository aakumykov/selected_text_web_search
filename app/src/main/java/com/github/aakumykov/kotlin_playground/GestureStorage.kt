package com.github.aakumykov.kotlin_playground

object GestureStorage {

    private val recordsList: MutableList<GestureRecord> = ArrayList()

    fun addRecord(gestureRecord: GestureRecord) {
        recordsList.add(gestureRecord)
    }
}