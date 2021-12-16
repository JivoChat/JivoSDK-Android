package com.jivosite.example

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.jivosite.example.MainActivity.Companion.ACTION_OPEN_CHAT
import com.jivosite.example.MainActivity.Companion.EXTRA_SHOW_PUSH
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
        Jivo.init(this, "Q7BcPYNqCG")
        Jivo.enableLogging()

        Jivo.setConfig(Config.Builder()
            .setOpenNotification {
                PendingIntent.getActivity(
                    this,
                    NOTIFICATION_REQUEST_CODE,
                    Intent(this, MainActivity::class.java).apply {
                        putExtra(ACTION_OPEN_CHAT, EXTRA_SHOW_PUSH)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            .build()
        )
    }
}
