package com.jivosite.sdk.model.repository.connection

/**
 * Created on 26.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
sealed class ConnectionState {
    object Initial : ConnectionState()
    object LoadConfig : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    data class Disconnected(val timeToReconnect: Long, val seconds: Long = 0) :
        ConnectionState()

    object Stopped : ConnectionState()

    data class Error(val timeToReconnect: Long, val seconds: Long = 0) :
        ConnectionState()
}
