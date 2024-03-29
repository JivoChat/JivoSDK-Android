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
 * @author Alexandr Shibelev (shibelev@jivosite.com)
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
        private const val LAST_UNREAD_MSG_ID = "lastUnreadMsgId"
        private const val LAST_ACK_MSG_ID = "lastMsgAckId"

        private const val NIGHT_MODE = "nightMode"

        private const val USER_TOKEN = "userToken"
        private const val PUSH_TOKEN = "pushToken"

        private const val IN_APP_NOTIFICATION_ENABLED = "notification"

        private const val HAS_SENT_CONTACT_INFO = "hasSentContactInfo"

        private const val CLIENT_MESSAGE = "clientMessage"

        private const val BLACKLISTED_TIME = "blacklistedTime"

        private const val SANCTIONED_TIME = "sanctionedTime"

        private const val HAS_SENT_PUSH_TOKEN = "hasSentPushToken"

        private const val CONTACT_INFO = "contactInfo"

        private const val CUSTOM_DATA = "customData"

        private const val HAS_SENT_CUSTOM_DATA = "hasSentCustomData"

        private const val CHAT_ID = "chatID"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var deviceId: String by SharedPreference(preferences, DEVICE_ID, "")

    var clientId: String by SharedPreference(preferences, CLIENT_ID, "")
    var path: String by SharedPreference(preferences, PATH, "")

    var startOnInitialization: Boolean by SharedPreference(
        preferences, START_ON_INITIALIZATION, BuildConfig.START_ON_INITIALIZATION
    )

    var chatserverHost: String by SharedPreference(preferences, CHATSERVER_HOST, "")
    var apiHost: String by SharedPreference(preferences, API_HOST, "")
    var filesHost: String by SharedPreference(preferences, FILES_HOST, "")

    var host: String by SharedPreference(preferences, HOST, "")
    var port: String by SharedPreference(preferences, PORT, "")
    var siteId: String by SharedPreference(preferences, SITE_ID, "")
    var widgetId: String by SharedPreference(preferences, WIDGET_ID, "")

    var userToken: String by SharedPreference(preferences, USER_TOKEN, "")
    var pushToken: String by SharedPreference(preferences, PUSH_TOKEN, "")

    var doNotShowPings: Boolean by SharedPreference(preferences, LOG_DO_NOT_SHOW_PINGS, false)

    var lastReadMsgId: Long by SharedPreference(preferences, LAST_READ_MSG_ID, 0)
    var lastUnreadMsgId: Long by SharedPreference(preferences, LAST_UNREAD_MSG_ID, 0)
    var lastAckMsgId: Long by SharedPreference(preferences, LAST_ACK_MSG_ID, 0)

    var inAppNotificationEnabled: Boolean by SharedPreference(preferences, IN_APP_NOTIFICATION_ENABLED, true)

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

    var clientMessage: String by SharedPreference(preferences, CLIENT_MESSAGE, "")

    var contactInfo: String by SharedPreference(preferences, CONTACT_INFO, "")

    var hasSentContactInfo: Boolean by SharedPreference(preferences, HAS_SENT_CONTACT_INFO, false)

    var blacklistedTime: Long by SharedPreference(preferences, BLACKLISTED_TIME, -1L)

    var sanctionedTime: Long by SharedPreference(preferences, SANCTIONED_TIME, -1L)

    var hasSentPushToken: Boolean by SharedPreference(preferences, HAS_SENT_PUSH_TOKEN, false)

    var customData: String by SharedPreference(preferences, CUSTOM_DATA, "")

    var hasSentCustomData: Boolean by SharedPreference(preferences, HAS_SENT_CUSTOM_DATA, false)

    var chatId: String by SharedPreference(preferences, CHAT_ID, "")

}
