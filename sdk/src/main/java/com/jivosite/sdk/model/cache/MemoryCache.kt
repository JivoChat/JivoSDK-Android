package com.jivosite.sdk.model.cache

import android.os.Looper
import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * Created on 2019-05-21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class MemoryCache<in K, V> @Inject constructor() {

    private val cache = ConcurrentHashMap<K, MutableLiveData<V>>()

    private fun isMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    @AnyThread
    fun clear() {
        if (isMainThread()) {
            cache.forEach { entry -> entry.value.value = null }
        } else {
            cache.forEach { entry -> entry.value.postValue(null) }
        }
        cache.clear()
    }

    @AnyThread
    @JvmOverloads
    operator fun get(key: K, defaultValue: V? = null): LiveData<V> {
        var cachedValue = cache[key]
        if (cachedValue == null) {
            cachedValue = MutableLiveData()
            if (isMainThread()) {
                cachedValue.value = defaultValue
            } else {
                cachedValue.postValue(defaultValue)
            }
            cache[key] = cachedValue
        }
        return cachedValue
    }

    @AnyThread
    operator fun set(key: K, value: V?) {
        var cachedValue = cache[key]
        if (cachedValue == null) {
            cachedValue = MutableLiveData()
            cache[key] = cachedValue
        }

        if (isMainThread()) {
            cachedValue.value = value
        } else {
            cachedValue.postValue(value)
        }
    }

    fun find(predicate: (V?) -> Boolean): V? {
        cache.values.forEach {
            if (predicate(it.value)) return it.value
        }
        return null
    }
}
