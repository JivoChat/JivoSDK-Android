package com.jivosite.sdk.socket.states

import javax.inject.Provider

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ServiceStateFactory(
    private val statesMap: Map<Class<out ServiceState>, @JvmSuppressWildcards Provider<ServiceState>>
) {

    fun <T : ServiceState> create(stateClass: Class<T>): T {
        val provider = statesMap[stateClass] ?: throw IllegalArgumentException("Unknown service state class $stateClass")
        return try {
            provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}