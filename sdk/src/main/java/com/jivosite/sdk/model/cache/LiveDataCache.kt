package com.jivosite.sdk.model.cache

import androidx.annotation.AnyThread
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

/**
 * Created on 28/04/2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class LiveDataCache<K, LD, T> @Inject constructor(private val producer: () -> LD) where LD : MutableLiveData<T> {

    private val cache = HashMap<K, LD>()

    @AnyThread
    operator fun get(key: K): LD {
        var value = cache[key]
        if (value == null) {
            value = producer()
            cache[key] = value
        }

        return value
    }

    fun put(key: K, value: LD) {
        cache[key] = value
    }

    fun put(key: K, value: T) {
        get(key).value = value
    }
}