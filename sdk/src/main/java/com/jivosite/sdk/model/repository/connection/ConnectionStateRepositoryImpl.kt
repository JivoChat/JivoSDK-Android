package com.jivosite.sdk.model.repository.connection

import androidx.lifecycle.LiveData
import com.jivosite.sdk.support.vm.CountDownLiveData
import javax.inject.Inject

/**
 * Created on 26.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ConnectionStateRepositoryImpl @Inject constructor(
) : ConnectionStateRepository {

    private val _state = CountDownLiveData()
    override val state: LiveData<ConnectionState>
        get() = _state

    override fun setState(state: ConnectionState) {
        _state.setState(state)
    }
}