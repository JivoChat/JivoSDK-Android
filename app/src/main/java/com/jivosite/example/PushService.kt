package com.jivosite.example

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.push.JivoPushProvider

/**
 * Created on 1/18/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class PushService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Jivo.subscribeToPush(token, JivoPushProvider.FCM)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Jivo.handleRemoteMessage(message.data)
    }
}