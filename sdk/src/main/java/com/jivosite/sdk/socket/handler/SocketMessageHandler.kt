package com.jivosite.sdk.socket.handler

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.socket.handler.delegates.FallbackDelegate
import com.jivosite.sdk.socket.obfuscate.MessageObfuscator
import com.squareup.moshi.Moshi

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class SocketMessageHandler(
    private val delegates: Map<String, SocketMessageDelegate>,
    private val fallbackDelegate: FallbackDelegate,
    private val parser: Moshi,
    private val obfuscator: MessageObfuscator
) {

    fun handle(msg: String) {
        if (msg.isBlank()) {
            Jivo.w("There is empty message from server, can't handle it")
            Jivo.w(" --> $msg")
            return
        }

        val message = try {
            parser.adapter(SocketMessage::class.java).fromJson(msg)
        } catch (e: Exception) {
            Jivo.e(e, "There is error on parse message")
            Jivo.e(" --> $msg")
            null
        }

        if (message == null || message.type.isBlank()) {
            Jivo.w("There is no necessary type field or message is null, can't handle it")
            Jivo.w(" --> $msg")
        } else {
            Jivo.i("""Try to handle socket message "${message.type}"""")
            Jivo.i(" - msg=${obfuscator.obfuscate(msg, message)}")

            val delegate = searchDelegate(message.type)
            if (delegate != null) {
                delegate.handle(message)
                Jivo.i("""Socket message has been successfully handled "${message.type}"""")
            } else {
                fallbackDelegate.handle(message)
            }
        }
    }

    /**
     *  Поиск делигата по типу.
     */
    private fun searchDelegate(type: String): SocketMessageDelegate? {
        return delegates[type] ?: delegates["${type.split("/").first()}/*"]
    }
}