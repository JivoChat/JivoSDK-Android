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
        Jivo.init(appContext = this)
        Jivo.setData("Q7BcPYNqCG")
        Jivo.setConfig(
            Config.Builder()
                .setWelcomeMessage(R.string.welcome)
                .setOnBackPressed {
                    activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                }
                .build()
        )
    }
}
