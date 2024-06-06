package com.github.aakumykov.kotlin_playground

object GestureRecordsStorage {

    private val recordsList: MutableList<GestureRecord> = ArrayList()

    fun addRecordIfNotNull(gestureRecord: GestureRecord?) {
        gestureRecord?.also { recordsList.add(it) }
    }

    fun getFirst(): GestureRecord? {
        return recordsList.firstOrNull()
    }

    fun clearAllRecords() {
        recordsList.clear()
    }
}