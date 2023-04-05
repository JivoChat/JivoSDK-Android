package com.jivosite.sdk.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.logger.LogsRepository
import com.jivosite.sdk.model.pojo.CustomData
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.builders.ContactInfo.Companion.contactInfo
import com.jivosite.sdk.support.ext.requireValue
import com.jivosite.sdk.support.usecase.SdkConfigUseCase
import javax.inject.Inject

/**
 * Created on 12/9/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoSettingsViewModel @Inject constructor(
    private val storage: SharedStorage,
    private val logsRepository: LogsRepository,
    private val sdkConfigUseCase: SdkConfigUseCase
) : ViewModel() {

    val host = MutableLiveData(storage.host)
    val widgetId = MutableLiveData(storage.widgetId)
    val userName = MutableLiveData("")
    val userEmail = MutableLiveData("")
    val userPhone = MutableLiveData("")
    val userDescription = MutableLiveData("")

    val title = MutableLiveData("")
    val key = MutableLiveData("")
    val content = MutableLiveData("")
    val link = MutableLiveData("")

    val userToken = MutableLiveData("")

    private val _doNotShowPings = MutableLiveData(storage.doNotShowPings)
    val doNotShowPings: LiveData<Boolean>
        get() = _doNotShowPings

    val hasNightModeChecked = MutableLiveData(storage.nightMode)

    fun saveAndRestart() {
        storage.host = host.requireValue().trim()

        Jivo.changeChannelId(widgetId.requireValue().trim())
    }

    fun clearAndRestart() {
        Jivo.clear()
    }

    fun setUserToken() {
        Jivo.setUserToken(userToken.requireValue())
    }

    fun sendUserInfo() {
        Jivo.setContactInfo(
            contactInfo {
                name = userName.value
                email = userEmail.value ?: ""
                phone = userPhone.value ?: ""
                description = userDescription.value
            }
        )
    }

    fun sendCustomData() {
        Jivo.setCustomData(
            listOf(
                CustomData(
                    title = title.value ?: "",
                    key = key.value ?: "",
                    content = content.value ?: "",
                    link = link.value ?: ""
                )
            )
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

    fun onNightModeChanged(isChecked: Boolean) {
        storage.changeNightMode(isChecked)
    }
}
