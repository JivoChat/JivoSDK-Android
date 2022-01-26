package com.jivosite.sdk.socket.obfuscate

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class DebugMessageObfuscator @Inject constructor() : MessageObfuscator {

    override fun obfuscate(msg: String, socketMessage: SocketMessage): String {
        return msg
    }
}