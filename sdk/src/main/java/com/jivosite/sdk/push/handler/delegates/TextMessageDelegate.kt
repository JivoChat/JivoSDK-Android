package com.jivosite.sdk.push.handler.delegates

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider.getUriForFile
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.push.PushData
import com.jivosite.sdk.push.handler.PushMessageDelegate.Companion.NOTIFICATION_MESSAGE_ID
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.ui.chat.JivoChatActivity
import java.io.File
import javax.inject.Inject


/**
 * Created on 1/20/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class TextMessageDelegate @Inject constructor(
    context: SdkContext,
    schedulers: Schedulers
) : GeneralPushMessageDelegate<TextMessageArgs>(context, schedulers) {

    companion object {
        private const val NOTIFICATIONS_DIR = "notifications"
        const val NOTIFICATION_REQUEST_CODE = 0
    }

    override fun getChannelId() = "jivo_sdk_message"

    @WorkerThread
    override fun doBeforeCreateChannel() {
        val names = context.appContext.assets.list(NOTIFICATIONS_DIR) ?: return
        val inputFiles = names.map { "$NOTIFICATIONS_DIR/$it" }

        val path = File(context.appContext.filesDir, NOTIFICATIONS_DIR)
        if (!path.exists()) {
            if (path.mkdirs()) {
                Jivo.i("Create directory for notifications sounds")
            }
        }

        names.forEachIndexed { position, name ->
            val outputFile = File(path, name)
            val outputFileUri =
                getUriForFile(context.appContext, "${context.appContext.packageName}.jivosdk.fileprovider", outputFile)

            with(context.appContext.contentResolver) {
                try {
                    openOutputStream(outputFileUri)?.use { outputStream ->
                        context.appContext.assets.open(inputFiles[position]).use { inputStream ->
                            val data = ByteArray(inputStream.available())
                            inputStream.read(data)

                            outputStream.write(data)
                            outputStream.flush()
                            Jivo.i("""Notification sound "$name" successfully copied to files dir""")
                        }
                    }
                } catch (e: Exception) {
                    Jivo.e("""Can not save sound "$name"""")
                }
            }
        }
    }

    @SuppressLint("NewApi")
    override fun createNotificationChannel() {
        val name = context.appContext.getString(R.string.notification_channel_message_name)
        val description = context.appContext.getString(R.string.notification_channel_message_description)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(getChannelId(), name, importance).also {
            it.description = description
            it.setSound(
                getSoundForChannel(),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun getSoundForChannel(): Uri {
        val path = File(context.appContext.filesDir, NOTIFICATIONS_DIR)
        val sound = File(path, "Jivo - Magic.mp3")
        return getUriForFile(context.appContext, "${context.appContext.packageName}.jivosdk.fileprovider", sound)
    }

    override fun getNotificationId() = NOTIFICATION_MESSAGE_ID

    override fun prepareArgs(data: PushData): TextMessageArgs {
        return TextMessageArgs(
            name = data.notification.args[0],
            message = data.notification.args[1]
        )
    }

    override fun createNotification(args: TextMessageArgs): Notification {
        val title = context.appContext.getString(R.string.notification_message_title)
        val text = context.appContext.getString(R.string.notification_message_text_format, args.name, args.message)
        return getDefaultNotificationBuilder()
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(getContentIntent())
            .apply {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    setSound(
                        Uri.parse(
                            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.appContext.packageName}/${R.raw.jivo_magic}"
                        )
                    )
                }
            }
            .build()
    }

    private fun getContentIntent(): PendingIntent {
        return Jivo.getConfig().openNotificationCallback?.invoke() ?: Intent(
            context.appContext,
            JivoChatActivity::class.java
        ).let {
            PendingIntent.getActivity(
                context.appContext,
                NOTIFICATION_REQUEST_CODE,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}

data class TextMessageArgs(
    val name: String,
    val message: String
)