package com.jivosite.sdk.model.repository

import android.os.Handler
import android.os.Looper
import com.jivosite.sdk.support.async.Schedulers
import java.util.concurrent.Executor

/**
 * Created on 12/13/20.
 *
 * Базовый класс для всех репозиториев библиотеки.
 * Для обработки всех сообщений внутри репозитория последовательно можно использовать метод [handleInRepositoryThread].
 * Если нужна обработка сообщения глобально, то используем метод [handleInDispatchingThread]. Он запустит обработку
 * в основном потоке приложения.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class GeneralRepository(schedulers: Schedulers, name: String) {

    private val handler = Handler(Looper.getMainLooper())

    private val executor: Executor = schedulers.createExecutorForRepository(name)
    private val dispatchingExecutor: Executor = schedulers.ui

    /**
     * Обработка сообщения во внутреннем потоке репозитория. Обработка идет в один поток, последовательно.
     * @param delay Задержка перед выполнением. Если не указана, то сразу выполняется.
     * @param runnable То что нужно выполнить.
     */
    protected fun handleInRepositoryThread(delay: Long = 0L, runnable: Runnable) {
        if (delay == 0L) {
            executor.execute(runnable)
        } else {
            handler.postDelayed({ executor.execute(runnable) }, delay)
        }
    }

    /**
     * Обработка сообщения в основном потоке приложения, нужно для диспетчеризации отдельных сообщений.
     */
    protected fun handleInDispatchingThread(runnable: Runnable) {
        dispatchingExecutor.execute(runnable)
    }

    /**
     * Очистка репозитория, если таковая необходима. Например при смене пользователя.
     */
    abstract fun clear()
}