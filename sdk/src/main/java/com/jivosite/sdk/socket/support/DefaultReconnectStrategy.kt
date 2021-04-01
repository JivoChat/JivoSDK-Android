package com.jivosite.sdk.socket.support

import javax.inject.Inject

/**
 * Created on 21.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class DefaultReconnectStrategy @Inject constructor() : ReconnectStrategy {

    companion object {
        private const val MIN_INTERVAL = 3000.0 // 3 sec
        private const val MAX_INTERVAL = 60000.0 // 1 min
    }

    private var previousTimeout = MIN_INTERVAL

    override fun nextTime(): Long {
        previousTimeout = (previousTimeout * (1 + Math.random())).coerceAtMost(MAX_INTERVAL)
        return previousTimeout.toLong()
    }

    override fun reset() {
        previousTimeout = MIN_INTERVAL
    }
}