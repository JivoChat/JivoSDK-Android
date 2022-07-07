package com.jivosite.sdk.push.handler.delegates

import android.app.Notification
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.push.PushData
import com.jivosite.sdk.push.handler.PushMessageDelegate
import com.jivosite.sdk.support.async.Schedulers

/**
 * Created on 1/20/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class GeneralPushMessageDelegate<P>(
    protected val context: SdkContext,
    protected val schedulers: Schedulers
) : PushMessageDelegate {

    protected val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context.appContext)
    }

    override fun handle(data: PushData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNotExists {
                showNotification(data)
            }
        } else {
            showNotification(data)
        }
    }

    private fun showNotification(data: PushData) {
        val notification = createNotification(prepareArgs(data))
        notificationManager.notify(getNotificationId(), notification)
    }

    private fun createChannelIfNotExists(doAfter: () -> Unit) {
        if (!channelExists(getChannelId())) {
            schedulers.io.execute {
                try {
                    doBeforeCreateChannel()
                } catch (e: Exception) {
                    Jivo.e(e, "Notification channel creation error")
                }
                schedulers.ui.execute {
                    createNotificationChannel()
                    doAfter()
                }
            }
        } else {
            doAfter()
        }
    }

    private fun channelExists(channelId: String): Boolean {
        return notificationManager.getNotificationChannel(channelId) != null
    }

    abstract fun getChannelId(): String

    abstract fun createNotificationChannel()

    abstract fun getNotificationId(): Int

    abstract fun prepareArgs(data: PushData): P

    abstract fun createNotification(args: P): Notification

    @WorkerThread
    open fun doBeforeCreateChannel() {

    }

    protected fun getDefaultNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context.appContext, getChannelId())
            .setSmallIcon(Jivo.getConfig().notificationSmallIcon ?: R.drawable.ic_jivo_sdk_notification_small)
            .setColor(ContextCompat.getColor(context.appContext, Jivo.getConfig().notificationColorIcon ?: R.color.darkPastelGreen))
            .setAutoCancel(true)
    }
}