package com.jivosite.example

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.jivosite.example.MainActivity.Companion.EXTRA_TARGET
import com.jivosite.example.MainActivity.Companion.TARGET_CHAT
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.push.handler.delegates.TextMessageDelegate.Companion.NOTIFICATION_REQUEST_CODE
import com.jivosite.sdk.support.builders.Config

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ExampleApplication : Application() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        Jivo.init(this)

        Jivo.setConfig(Config.Builder()
            .setOpenNotification {
                PendingIntent.getActivity(
                    this,
                    NOTIFICATION_REQUEST_CODE,
                    Intent(this, MainActivity::class.java).apply {
                        putExtra(EXTRA_TARGET, TARGET_CHAT)
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            .build()
        )
    }
}
