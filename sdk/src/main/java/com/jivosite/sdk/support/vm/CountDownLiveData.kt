package com.jivosite.sdk.support.vm

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.repository.connection.ConnectionState

/**
 * Created on 27.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class CountDownLiveData : LiveData<ConnectionState>() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var countDownTimer: CountDownTimer? = null

    init {
        value = ConnectionState.Initial
    }

    fun setState(state: ConnectionState) {
        postValue(state)
        if (hasActiveObservers()) {
            startCountDownIfNeeded(state)
        }
    }

    private fun startCountDownIfNeeded(state: ConnectionState) {
        countDownTimer?.cancel()
        countDownTimer = null

        if (state is ConnectionState.Disconnected) {
            val leftTime = state.timeToReconnect - System.currentTimeMillis()
            if (state.timeToReconnect == 0L || leftTime <= 1000) {
                postValue(state.copy(seconds = 0))
                return
            }

            postValue(state.copy(seconds = leftTime / 1000))
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                start(leftTime)
            } else {
                handler.post { start(leftTime) }
            }
        }

        if (state is ConnectionState.Error) {
            val leftTime = state.timeToReconnect - System.currentTimeMillis()
            if (state.timeToReconnect == 0L || leftTime <= 1000) {
                postValue(state.copy(seconds = 0))
                return
            }

            postValue(state.copy(seconds = leftTime / 1000))
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                start(leftTime)
            } else {
                handler.post { start(leftTime) }
            }
        }


    }

    @MainThread
    private fun start(millisInFuture: Long) {
        try {
            countDownTimer = object : CountDownTimer(millisInFuture, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    updateState(millisUntilFinished)
                }

                override fun onFinish() {
                    updateState(0)
                    countDownTimer = null
                }

                private fun updateState(millisUntilFinished: Long) {
                    val currentState = value
                    if (currentState is ConnectionState.Disconnected) {
                        postValue(currentState.copy(seconds = millisUntilFinished / 1000))
                    }
                    if (currentState is ConnectionState.Error) {
                        postValue(currentState.copy(seconds = millisUntilFinished / 1000))
                    }
                }
            }.start()
        } catch (e: Exception) {
            Jivo.e(e, "CountDownLiveData: cat not start timer")
        }
    }

    override fun onActive() {
        super.onActive()
        value?.run { startCountDownIfNeeded(this) }
    }

    override fun onInactive() {
        super.onInactive()
        countDownTimer?.cancel()
        countDownTimer = null
    }
}