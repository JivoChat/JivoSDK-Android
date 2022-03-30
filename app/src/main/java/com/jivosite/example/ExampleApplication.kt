package com.jivosite.example

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.jivosite.example.MainActivity.Companion.EXTRA_TARGET
import com.jivosite.example.MainActivity.Companion.TARGET_CHAT
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.push.handler.delegates.TextMessageDelegate.Companion.NOTIFICATION_REQUEST_CODE
import com.jivosite.sdk.support.builders.Config
import timber.log.Timber

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Jivo.init(this, "1KVGmjYofK", "androidsdk.dev.jivosite.com")
        Jivo.enableLogging()

        Jivo.setConfig(Config.Builder()
            .setOpenNotification {
                PendingIntent.getActivity(
                    this,
                    NOTIFICATION_REQUEST_CODE,
                    Intent(this, MainActivity::class.java).apply {
                        putExtra(EXTRA_TARGET, TARGET_CHAT)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            .build()
        )
    }
}
