package com.jivosite.sdk.model.repository.connection

/**
 * Created on 26.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
sealed class ConnectionState {
    object Initial : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    data class Disconnected(val timeToReconnect: Long, val seconds: Long = 0) : ConnectionState()
    object Stopped : ConnectionState()
}
