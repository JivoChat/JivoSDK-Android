package com.jivosite.sdk.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jivosite.sdk.Jivo

/**
 * Created on 1/15/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Deprecated(
    message = "Provider-specific helper, will be removed in the next major release. Call Jivo.subscribeToPush() and Jivo.handleRemoteMessage() from your push provider's MessagingService instead."
)
open class JivoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Jivo.subscribeToPush(token, JivoPushProvider.FCM)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Jivo.handleRemoteMessage(message.data)
    }
}