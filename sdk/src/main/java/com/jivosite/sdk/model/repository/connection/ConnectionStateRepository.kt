package com.jivosite.sdk.model.repository.connection

import androidx.lifecycle.LiveData

/**
 * Created on 26.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface ConnectionStateRepository {

    val state: LiveData<ConnectionState>

    fun setState(state: ConnectionState)
}