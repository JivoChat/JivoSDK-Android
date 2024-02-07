package com.jivosite.sdk.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jivosite.sdk.Jivo

/**
 * Created on 1/15/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
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