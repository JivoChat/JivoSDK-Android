package com.jivosite.sdk.socket.endpoint

import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import java.net.URI
import javax.inject.Inject

/**
 * Created on 05.12.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class DefaultSocketEndpointProvider @Inject constructor(
    private val sdkContext: SdkContext,
    private val storage: SharedStorage,
) : SocketEndpointProvider {

    override fun getEndpoint(): URI {
        return storage.path.let {
            if (it.isBlank()) {
                URI.create(
                    "wss://${storage.chatserverHost}/atom/" +
                            "${storage.siteId}:" +
                            storage.widgetId.ifBlank { sdkContext.widgetId } +
                            getQuery(storage.userToken)
                )
            } else {
                URI.create("wss://${storage.chatserverHost}/atom${it}")
            }
        }
    }

    private fun getQuery(userToken: String) = if (userToken.isNotBlank()) """?token=${userToken}""" else ""
}
