package androidx.lifecycle

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.jivosite.sdk.Jivo

/**
 * Created on 13.09.2021.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
object JivoLifecycleOwner : LifecycleOwner {

    private var resumedCounter = 0
    private var pauseSent = true

    private val registry = LifecycleRegistry(this)

    private val initializationListener = object : ReportFragment.ActivityInitializationListener {

        override fun onCreate() = Unit

        override fun onStart() = Unit

        override fun onResume() {
            Jivo.d("New lifecycle - on activity resumed")
            activityResumed()
        }
    }

    fun init(context: Context) {
        Jivo.d("New lifecycle - init")
        val app = context.applicationContext as Application
        app.registerActivityLifecycleCallbacks(object : JivoActivityLifecycleCallbacks() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onActivityPreCreated(a: Activity, savedInstanceState: Bundle?) {
                Jivo.d("New lifecycle - on activity pre created")
                a.registerActivityLifecycleCallbacks(object : JivoActivityLifecycleCallbacks() {
                    override fun onActivityPostResumed(activity: Activity) {
                        Jivo.d("New lifecycle - on activity post resumed")
                        activityResumed()
                    }
                })
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (Build.VERSION.SDK_INT < 29) {
                    Jivo.d("New lifecycle - on activity created")
                    ReportFragment.get(activity).setProcessListener(initializationListener)
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (Build.VERSION.SDK_INT < 29) {
                    Jivo.d("New lifecycle - on activity paused")
                    activityPaused()
                }
            }

            override fun onActivityPrePaused(activity: Activity) {
                Jivo.d("New lifecycle - on activity pre paused")
                activityPaused()
            }
        })
    }

    private fun activityResumed() {
        resumedCounter++
        if (resumedCounter == 1 && pauseSent) {
            Jivo.d("New lifecycle - handle event ON_START")
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            pauseSent = false
        }
    }

    private fun activityPaused() {
        resumedCounter--
        if (resumedCounter == 0 && !pauseSent) {
            Jivo.d("New lifecycle - handle event ON_STOP")
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