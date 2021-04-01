package com.jivosite.sdk.model.storage

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created on 2019-08-05.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class SharedPreference<T>(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defValue: T
) : ReadWriteProperty<Any?, T> {

    private var currentValue: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (currentValue == null) {
            currentValue = preferences.run {
                when (defValue) {
                    is Boolean -> getBoolean(name, defValue)
                    is Int -> getInt(name, defValue)
                    is Long -> getLong(name, defValue)
                    is Float -> getFloat(name, defValue)
                    is String -> getString(name, defValue)
                    else -> throw IllegalArgumentException("Unsupported type")
                } as T
            }
        }
        return currentValue ?: defValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        currentValue = value
        preferences.edit().apply {
            when (value) {
                is Boolean -> putBoolean(name, value)
                is Int -> putInt(name, value)
                is Long -> putLong(name, value)
                is Float -> putFloat(name, value)
                is String -> putString(name, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }.apply()
    }
}