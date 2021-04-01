package com.jivosite.sdk.support.vm

import android.os.Looper
import androidx.lifecycle.MutableLiveData

/**
 * Created on 1/27/21.
 *
 * LiveData для состояний, внутри репозитория. Всегда имеет значение.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class StateLiveData<T>(state: T) : MutableLiveData<T>(state) {

    override fun getValue(): T = super.getValue() ?: throw IllegalStateException("Value in StateLiveData must not be null")

    /**
     * Если метод вызывается не из главного потока, то вместо setValue будет вызван [postValue]
     */
    override fun setValue(value: T) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            super.setValue(value)
        } else {
            super.postValue(value)
        }
    }
}