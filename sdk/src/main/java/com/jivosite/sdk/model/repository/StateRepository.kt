package com.jivosite.sdk.model.repository

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 1/27/21.
 *
 * Базовый класс для всех репозиториев библиотеки с состоянием.
 *
 * @param STATE Состояние репозитория, которое можно обновить через [updateState]
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class StateRepository<STATE>(
    schedulers: Schedulers,
    name: String,
    initialState: STATE
) : GeneralRepository(schedulers, name) {

    protected var _state: STATE = initialState
    protected val _stateLive = StateLiveData(_state)

    /**
     * Обновление состояния во внутреннем потоке репозитория. Обработка идет в один поток, последовательно.
     * @param handler В качестве параметра передается [Action].
     */
    protected fun updateStateInRepositoryThread(delay: Long = 0L, handler: Action<STATE>.() -> Unit) {
        handleInRepositoryThread(delay) {
            updateState(handler)
        }
    }

    /**
     * Обновление состояния в основном потоке приложения.
     * @param handler В качестве параметра передается [Action].
     */
    protected fun updateStateInDispatchingThread(handler: Action<STATE>.() -> Unit) = handleInDispatchingThread {
        updateState(handler)
    }

    /**
     * Обновление внутреннего состояния репозитория.
     * @param handler В качестве параметра передается [Action].
     */
    private fun updateState(handler: Action<STATE>.() -> Unit) {
        val action = Action(_state)
        handler(action)
        val (needUpdate, newState) = action.handle()
        if (needUpdate && newState != null) {
            updateState(newState)
        }
    }

    /**
     * Обновление внутреннего состояния репозитория.
     * @param state Внутреннее состояние репозитория, которое станет актуальным.
     */
    private fun updateState(state: STATE) {
        Jivo.d("Update state to $state")
        _state = state
        _stateLive.value = state
    }

    /**
     * Выполенение действий, для обработки и обновления состояния репозитория.
     * @param state Состояние, с которым будут произведены действия.
     */
    class Action<STATE>(private val state: STATE) {
        private var doBefore: ((STATE) -> Boolean)? = null
        private var transform: ((STATE) -> STATE)? = null
        private var doAfter: ((STATE) -> Unit)? = null

        /**
         * Выполняется перед основным преобразованием. Если возвращается false, то дальнейшие действия
         * отменяются. Может быть не объявлен.
         */
        fun doBefore(call: (STATE) -> Boolean): Action<STATE> {
            this.doBefore = call
            return this
        }

        /**
         * Основное преобразование состояния. На вход подается старое состояние, на выходе должно быть новое,
         * которое в дальнейшем станет актуальным.
         */
        fun transform(call: (STATE) -> STATE): Action<STATE> {
            this.transform = call
            return this
        }

        /**
         * Выполняется после основного преобразования состояния.
         */
        fun doAfter(call: (STATE) -> Unit): Action<STATE> {
            this.doAfter = call
            return this
        }

        /**
         * Выполнение объявленных действий.
         */
        fun handle(): Pair<Boolean, STATE?> {
            return if (doBefore?.invoke(state) != false) {
                val transform = requireNotNull(this.transform) { "You need to implement transform method" }
                val newState = transform(state)
                doAfter?.invoke(newState)
                true to newState
            } else {
                false to null
            }
        }
    }
}