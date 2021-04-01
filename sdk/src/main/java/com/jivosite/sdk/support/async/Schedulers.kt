package com.jivosite.sdk.support.async

import java.util.concurrent.Executor

/**
 * Created on 18.11.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface Schedulers {

    val io: Executor

    val network: Executor

    val ui: Executor

    fun createExecutorForRepository(name: String): Executor
}