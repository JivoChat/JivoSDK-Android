package com.jivosite.sdk.socket.states

/**
 * Created on 26.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
sealed class DisconnectReason {

    object Stopped : DisconnectReason() {
        override fun toString(): String {
            return "Stopped"
        }
    }

    data class DisconnectedByServer(val code: Int, val message: String) : DisconnectReason()

    data class BadInstance(val host: String) : DisconnectReason()

    object ChangeInstance : DisconnectReason() {
        override fun toString(): String {
            return "ChangeInstance"
        }
    }

    object Unknown : DisconnectReason() {
        override fun toString(): String {
            return "Unknown"
        }
    }
}
