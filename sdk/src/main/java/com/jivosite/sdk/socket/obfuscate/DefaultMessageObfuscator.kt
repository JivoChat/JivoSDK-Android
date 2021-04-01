package com.jivosite.sdk.socket.obfuscate

import com.jivosite.sdk.BuildConfig
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class DefaultMessageObfuscator @Inject constructor() : MessageObfuscator {

    override fun obfuscate(msg: String, socketMessage: SocketMessage): String {
        return if (BuildConfig.DEBUG) {
            msg
        } else {
            // TODO Сделать обфускацию сообщений для отладки, скрывать только критичные данные
            "{ *** }"
        }
    }
}