package com.jivosite.sdk.socket.endpoint

import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import java.net.URI
import javax.inject.Inject

/**
 * Created on 05.12.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class DefaultSocketEndpointProvider @Inject constructor(
        private val sdkContext: SdkContext,
        private val storage: SharedStorage,
) : SocketEndpointProvider {

    override fun getEndpoint(): URI {
        return storage.path.let {
            if (it.isBlank()) {
                URI.create("wss://${storage.chatserverHost}/atom/${storage.siteId}:${sdkContext.widgetId}")
            } else {
                URI.create("wss://${storage.chatserverHost}/atom${it}")
            }
        }
    }
}