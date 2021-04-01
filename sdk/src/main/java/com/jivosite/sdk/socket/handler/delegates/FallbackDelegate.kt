package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.socket.handler.SocketMessageDelegate

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class FallbackDelegate : SocketMessageDelegate {

    override fun handle(message: SocketMessage) {
        Jivo.w("""There is unhandled message "$message"""")
    }
}