package com.example.blueprint.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TestObserver<T> : Observer<T> {
    val observedValues = mutableListOf<T>()
    override fun onChanged(t: T) {
        observedValues.add(t)
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}
