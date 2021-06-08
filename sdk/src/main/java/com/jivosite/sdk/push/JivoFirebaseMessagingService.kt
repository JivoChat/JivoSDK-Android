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

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Jivo.updatePushToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Jivo.handleRemoteMessage(message)
    }
}