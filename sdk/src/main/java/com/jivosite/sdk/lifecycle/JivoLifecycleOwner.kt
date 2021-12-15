package com.jivosite.sdk.lifecycle

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.jivosite.sdk.Jivo

/**
 * Created on 13.09.2021.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Deprecated("not worked")
object JivoLifecycleOwner : LifecycleOwner {

    private var resumedCounter = 0
    private var pauseSent = true

    private val registry = LifecycleRegistry(this)

    @SuppressLint("LogNotTimber")
    fun init(context: Context) {
        Log.d("JivoSDK", "JivoLifecycle: Owner - init")
        val app = context.applicationContext as Application
        app.registerActivityLifecycleCallbacks(object : JivoActivityLifecycleCallbacks() {

            override fun onActivityResumed(activity: Activity) {
                if (Build.VERSION.SDK_INT < 29) {
                    Jivo.d("JivoLifecycle: ActivityLifecycleCallbacks(app) - on resumed")
                    activityResumed()
                }
            }

            override fun onActivityPostResumed(activity: Activity) {
                Jivo.d("JivoLifecycle: ActivityLifecycleCallbacks(app) - on post resumed")
                activityResumed()
            }

            override fun onActivityPaused(activity: Activity) {
                if (Build.VERSION.SDK_INT < 29) {
                    Jivo.d("JivoLifecycle: ActivityLifecycleCallbacks(app) - on paused")
                    activityPaused()
                }
            }

            override fun onActivityPrePaused(activity: Activity) {
                Jivo.d("JivoLifecycle: ActivityLifecycleCallbacks(app) - on pre paused")
                activityPaused()
            }
        })
    }

    private fun activityResumed() {
        resumedCounter++
        if (resumedCounter == 1 && pauseSent) {
            Jivo.d("JivoLifecycle: dispatch event ON_START")
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            pauseSent = false
        }
    }

    private fun activityPaused() {
        resumedCounter--
        if (resumedCounter == 0 && !pauseSent) {
            Jivo.d("JivoLifecycle: dispatch event ON_STOP")
            pauseSent = true
            registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    override fun getLifecycle() = registry

    fun addObserver(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }
}

internal open class JivoActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit
}