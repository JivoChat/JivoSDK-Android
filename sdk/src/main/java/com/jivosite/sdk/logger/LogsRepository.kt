package com.jivosite.sdk.logger

import androidx.lifecycle.LiveData

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface LogsRepository {

    val messages: LiveData<List<LogMessage>>

    fun addMessage(message: LogMessage)

    fun refresh()

    fun clear()
}