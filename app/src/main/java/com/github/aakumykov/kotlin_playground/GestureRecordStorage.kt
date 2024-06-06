package com.github.aakumykov.kotlin_playground

object GestureRecordStorage {

    private val recordsList: MutableList<GestureRecord> = ArrayList()

    fun addRecordIfNotNull(gestureRecord: GestureRecord?) {
        gestureRecord?.also { recordsList.add(it) }
    }

    fun popFirst(): GestureRecord? {
        return recordsList.removeFirstOrNull()
    }

    fun clearAllRecords() {
        recordsList.clear()
    }
}