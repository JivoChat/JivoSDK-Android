package com.jivosite.sdk.socket.states

/**
 * Created on 25.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface ServiceStateContext {

    fun getState(): ServiceState

    fun changeState(state: Class<out ServiceState>): ServiceState
}