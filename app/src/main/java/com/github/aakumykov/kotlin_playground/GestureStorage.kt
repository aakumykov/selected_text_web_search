package com.github.aakumykov.kotlin_playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object GestureStorage {

    private val _lastGesture: MutableLiveData<String> = MutableLiveData()
    val lastGesture: LiveData<String> = _lastGesture

    fun addGesture(g: String) {
        _lastGesture.postValue(g)
    }
}