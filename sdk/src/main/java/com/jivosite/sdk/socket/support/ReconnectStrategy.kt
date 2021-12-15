package com.jivosite.sdk.socket.support

/**
 * Created on 21.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface ReconnectStrategy {

    fun nextTime(): Long

    fun reset()
}