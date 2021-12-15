package com.jivosite.sdk.socket.endpoint

import java.net.URI

/**
 * Created on 05.12.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface SocketEndpointProvider {
    fun getEndpoint(): URI
}