package com.jivosite.sdk.socket.endpoint

import com.jivosite.sdk.Jivo
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
    private val storage: SharedStorage
) : SocketEndpointProvider {

    override fun getEndpoint(): URI {
        return if (storage.host.isBlank()) {
            createProdUri()
        } else {
            try {
                createDebugUri()
            } catch (e: Exception) {
                Jivo.e(e, "There is error in web socket uri")
                createProdUri()
            }
        }
    }

    private fun createProdUri(): URI {
        return storage.path.let {
            if (it.isBlank()) {
                URI.create("wss://node.jivosite.com/atom/${sdkContext.siteId}:${sdkContext.widgetId}")
            } else {
                URI.create("wss://node.jivosite.com/atom${it}")
            }
        }
    }

    private fun createDebugUri(): URI {
        val route = "wss://${storage.host}:${storage.port}/atom"
        return storage.path.let {
            if (it.isBlank()) {
                URI.create("$route/${storage.siteId}:${storage.widgetId}")
            } else {
                URI.create("$route${it}")
            }
        }
    }
}