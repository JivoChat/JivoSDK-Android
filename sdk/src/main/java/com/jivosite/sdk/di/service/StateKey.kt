package com.jivosite.sdk.di.service

import com.jivosite.sdk.socket.states.ServiceState
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created on 11.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
internal annotation class StateKey(val value: KClass<out ServiceState>)