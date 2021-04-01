package com.jivosite.sdk.support.async

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created on 18.11.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class SchedulersImpl @JvmOverloads constructor(networkPoolSize: Int = 3) : Schedulers {

    companion object {
        private val networkThreadsCount = AtomicInteger(0)
    }

    private val networkThreadGroup = ThreadGroup("Network")

    override val ui: Executor = UIExecutor()
    override val io: Executor = Executors.newSingleThreadExecutor { r -> Thread(r, "IO-Thread") }
    override val network: Executor = Executors.newFixedThreadPool(networkPoolSize) { r ->
        val newThreadNumber = networkThreadsCount.incrementAndGet()
        Thread(networkThreadGroup, r, "NetworkThread-$newThreadNumber")
    }

    override fun createExecutorForRepository(name: String): Executor {
        return Executors.newSingleThreadExecutor { r -> Thread(r, "Repository-Thread-$name") }
    }

    private class UIExecutor : Executor {

        private val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
}