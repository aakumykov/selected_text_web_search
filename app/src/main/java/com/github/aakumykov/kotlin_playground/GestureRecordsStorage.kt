package com.github.aakumykov.kotlin_playground

object GestureRecordsStorage {

    private val recordsList: MutableList<GestureRecord> = ArrayList()

    fun addRecord(gestureRecord: GestureRecord) {
        recordsList.add(gestureRecord)
    }

    fun getFirst(): GestureRecord? {
        return recordsList.firstOrNull()
    }

    fun clearAllRecords() {
        recordsList.clear()
    }
}