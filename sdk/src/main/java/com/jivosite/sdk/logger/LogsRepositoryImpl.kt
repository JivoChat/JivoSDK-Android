package com.jivosite.sdk.logger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jivosite.sdk.support.async.Schedulers
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class LogsRepositoryImpl @Inject constructor(
    schedulers: Schedulers
) : LogsRepository {

    private val executor: Executor = schedulers.createExecutorForRepository("Logs")

    private var list = LinkedList<LogMessage>()

    private val _messages = MutableLiveData<List<LogMessage>>(list)
    override val messages: LiveData<List<LogMessage>>
        get() = _messages

    override fun addMessage(message: LogMessage) {
        executor.execute {
            list = copyList().apply { add(message) }
            _messages.postValue(list)
        }
    }

    override fun refresh() {
        _messages.postValue(_messages.value)
    }

    override fun clear() {
        executor.execute {
            list = LinkedList()
            _messages.postValue(list)
        }
    }

    private fun copyList(): LinkedList<LogMessage> {
        return LinkedList<LogMessage>(list)
    }
}