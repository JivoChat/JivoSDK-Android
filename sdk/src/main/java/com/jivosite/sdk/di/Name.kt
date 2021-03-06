package com.jivosite.sdk.di

import javax.inject.Qualifier

/**
 * Created on 08.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Name(val value: String = "")