package com.jivosite.example

import android.app.Application
import com.jivosite.sdk.Jivo
import timber.log.Timber

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Jivo.init(this, "Q7BcPYNqCG")
    }
}
