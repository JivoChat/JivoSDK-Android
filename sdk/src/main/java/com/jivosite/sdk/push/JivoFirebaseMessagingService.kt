package com.jivosite.sdk.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.push.Notification
import com.jivosite.sdk.model.pojo.push.PushData
import com.jivosite.sdk.model.pojo.push.U
import com.jivosite.sdk.push.handler.PushMessageHandler
import com.jivosite.sdk.support.usecase.UpdatePushTokenUseCase
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created on 1/15/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
open class JivoFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var updatePushTokenUseCaseProvider: Provider<UpdatePushTokenUseCase>

    @Inject
    lateinit var parser: Moshi

    @Inject
    lateinit var handler: PushMessageHandler

    override fun onCreate() {
        super.onCreate()
        Jivo.getPushServiceComponent(this).inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Jivo.clearPushServiceComponent()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updatePushTokenUseCaseProvider.get().execute(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Jivo.i("Received push message")

        val data = try {
            extractData(message)
        } catch (e: Exception) {
            Jivo.e(e, "Push message parsing problem")
            return
        }

        handler.handle(data)
    }

    private fun extractData(message: RemoteMessage): PushData {
        // Extract "u" field from remote message
        val u = message.data["u"]?.let {
            parser.adapter(U::class.java).fromJson(it)
        }

        // Extract "notification" field from remote message
        val notification = message.data["notification"]?.let {
            parser.adapter(Notification::class.java).fromJson(it)
        }

        return PushData(
            requireNotNull(u) { """There is no "u" field in push message""" },
            requireNotNull(notification) { """There is no "notification" field in push message""" }
        )
    }
}