package com.jivosite.sdk.model.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jivosite.sdk.BuildConfig

/**
 * Created on 2019-06-05.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class SharedStorage(context: Context) {

    companion object {
        private const val FILE_NAME = "com.jivosite.sdk.session"

        private const val DEVICE_ID = "deviceId"

        private const val CLIENT_ID = "clientId"
        private const val PATH = "path"
        private const val START_ON_INITIALIZATION = "startOnInitialization"

        private const val CHATSERVER_HOST = "chatserverHost"
        private const val API_HOST = "apiHost"
        private const val FILES_HOST = "filesHost"

        private const val HOST = "host"
        private const val PORT = "port"
        private const val SITE_ID = "siteId"
        private const val WIDGET_ID = "widgetId"

        private const val LOG_DO_NOT_SHOW_PINGS = "doNotShowPings"

        private const val LAST_READ_MSG_ID = "lastReadMsgId"

        private const val NIGHT_MODE = "nightMode"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var deviceId: String by SharedPreference(preferences, DEVICE_ID, "")

    var clientId: String by SharedPreference(preferences, CLIENT_ID, "")
    var path: String by SharedPreference(preferences, PATH, "")

    var startOnInitialization: Boolean by SharedPreference(
        preferences,
        START_ON_INITIALIZATION,
        BuildConfig.START_ON_INITIALIZATION
    )

    var chatserverHost: String by SharedPreference(preferences, CHATSERVER_HOST, "")
    var apiHost: String by SharedPreference(preferences, API_HOST, "")
    var filesHost: String by SharedPreference(preferences, FILES_HOST, "")

    var host: String by SharedPreference(preferences, HOST, "")
    var port: String by SharedPreference(preferences, PORT, "")
    var siteId: String by SharedPreference(preferences, SITE_ID, "")
    var widgetId: String by SharedPreference(preferences, WIDGET_ID, "")

    var doNotShowPings: Boolean by SharedPreference(preferences, LOG_DO_NOT_SHOW_PINGS, false)

    var lastReadMsgId: Long by SharedPreference(preferences, LAST_READ_MSG_ID, 0)

    private var nightModePreference by SharedPreference(preferences, NIGHT_MODE, false)
    private var _nightMode = MediatorLiveData<Boolean>().apply {
        value = nightModePreference
        addSource(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    val nightMode: LiveData<Boolean>
        get() = _nightMode

    fun changeNightMode(hasNightModeEnable: Boolean) {
        nightModePreference = hasNightModeEnable
        _nightMode.value = hasNightModeEnable
    }
}
