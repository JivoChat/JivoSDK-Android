package com.jivosite.sdk.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.logger.LogsRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.ext.requireValue
import javax.inject.Inject

/**
 * Created on 12/9/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoSettingsViewModel @Inject constructor(
    private val storage: SharedStorage,
    private val logsRepository: LogsRepository
) : ViewModel() {

    val host = MutableLiveData(storage.host)
    val widgetId = MutableLiveData(storage.widgetId)

    val userName = MutableLiveData("")
    val userEmail = MutableLiveData("")
    val userPhone = MutableLiveData("")
    val userDescription = MutableLiveData("")

    private val _doNotShowPings = MutableLiveData(storage.doNotShowPings)
    val doNotShowPings: LiveData<Boolean>
        get() = _doNotShowPings

    private val _restart = MutableLiveData(false)
    val restart: LiveData<Boolean>
        get() = _restart

    fun saveAndRestart() {
        storage.host = host.requireValue().trim()
        storage.widgetId = widgetId.requireValue().trim()

        storage.path = ""

        _restart.value = true
    }

    fun clearAndRestart() {
        host.value = ""
        widgetId.value = ""

        saveAndRestart()
    }

    fun sendUserInfo() {
        Jivo.setClientInfo(
            userName.value ?: "",
            userEmail.value ?: "",
            userPhone.value ?: "",
            userDescription.value ?: ""
        )
    }

    fun changeDoNotShowPings() {
        val doNotShowPings = !_doNotShowPings.requireValue()
        storage.doNotShowPings = doNotShowPings
        _doNotShowPings.value = doNotShowPings
        logsRepository.refresh()
    }

    fun clearLogs() {
        logsRepository.clear()
    }
}