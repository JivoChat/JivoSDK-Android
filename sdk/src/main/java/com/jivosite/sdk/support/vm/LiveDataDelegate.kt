package com.jivosite.sdk.support.vm

import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created on 2019-08-05.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class LiveDataDelegate<T>(private val liveData: MutableLiveData<T>,
                          private val defaultValue: T) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return liveData.value ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        liveData.value = value
    }
}