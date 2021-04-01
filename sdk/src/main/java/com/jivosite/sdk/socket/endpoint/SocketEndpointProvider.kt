package com.jivosite.sdk.socket.endpoint

import java.net.URI

/**
 * Created on 05.12.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface SocketEndpointProvider {
    fun getEndpoint(): URI
}