package com.jivosite.sdk.support.livedata

import androidx.lifecycle.MediatorLiveData

/**
 * Created on 2/5/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientTypingDebounceLiveData<T>(private val timeout: Long = 3000) : MediatorLiveData<T>() {

    private var isWaitTime = false
    private var timeInterval = 0L

    override fun setValue(value: T) {
        waitIfNeeded(value) { v -> super.setValue(v) }
    }

    override fun postValue(value: T) {
        waitIfNeeded(value) { v -> super.postValue(v) }
    }

    private fun waitIfNeeded(value: T, action: (T) -> Unit) {
        if (!isWaitTime) {
            timeInterval = System.currentTimeMillis()
            isWaitTime = true
            action(value)
        } else if (System.currentTimeMillis() >= (timeInterval + timeout)) {
            isWaitTime = false
            timeInterval = System.currentTimeMillis()
        }
    }
}