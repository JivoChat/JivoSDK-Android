package com.jivosite.jivosdk_android_sample

import android.app.Application
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.support.builders.Config

/**
 * Created on 4/19/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Jivo.init(
            appContext = this,
            widgetId = "4UoDFh5U7n"
        )
        Jivo.setConfig(
            Config.Builder()
                .setLogo(R.drawable.vic_jivosdk_logo)
                .setBackground(R.drawable.bg_jivosdk_appbar)
                .setTitle(R.string.jivosdk_title)
                .setSubtitle(R.string.jivosdk_subtitle)
                .setWelcomeMessage(R.string.jivosdk_welcome)
                .build()
        )
    }
}
