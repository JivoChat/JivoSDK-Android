package com.jivosite.sdk.support.vm

import androidx.lifecycle.LiveData

/**
 * Created on 30.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AbsentLiveData<T> private constructor() : LiveData<T>() {

    companion object {
        fun <T> create(): LiveData<T> = AbsentLiveData()
    }

    init {
        value = null
    }
}