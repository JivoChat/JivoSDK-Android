package com.jivosite.sdk.socket.obfuscate

import com.jivosite.sdk.model.pojo.socket.SocketMessage

/**
 * Created on 12/5/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface MessageObfuscator {
    fun obfuscate(msg: String, socketMessage: SocketMessage): String
}