package com.jivosite.sdk.socket.transmitter

import com.jivosite.sdk.model.pojo.socket.SocketMessage

/**
 * Created on 12/15/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface TransmitterSubscriber {

    fun sendMessage(message: SocketMessage)

    fun sendMessage(message: String)
}