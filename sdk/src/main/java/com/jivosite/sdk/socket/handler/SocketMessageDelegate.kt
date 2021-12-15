package com.jivosite.sdk.socket.handler

import com.jivosite.sdk.model.pojo.socket.SocketMessage

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface SocketMessageDelegate {

    fun handle(message: SocketMessage)
}