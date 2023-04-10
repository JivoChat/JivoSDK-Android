package com.jivosite.sdk.socket.states

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class ServiceState(protected val stateContext: ServiceStateContext) {

    abstract fun load()

    abstract fun start()

    abstract fun reconnect(force: Boolean)

    abstract fun setConnected()

    abstract fun send(message: SocketMessage)

    abstract fun send(message: String)

    abstract fun stop()

    abstract fun setDisconnected(reason: DisconnectReason)

    abstract fun restart()

    abstract fun error(reason: String)

    override fun toString(): String {
        return this.javaClass.simpleName
    }

    fun logImpossibleAction(method: String) {
        Jivo.e("Call $method on $this, something wrong")
    }
}