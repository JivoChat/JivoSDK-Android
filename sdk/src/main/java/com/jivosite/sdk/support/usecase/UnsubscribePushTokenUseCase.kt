package com.jivosite.sdk.support.usecase

import androidx.lifecycle.LiveData
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.api.PushApi
import com.jivosite.sdk.model.pojo.push.Device
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.network.resource.NetworkResource
import com.jivosite.sdk.network.resource.Resource
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.ext.loadSilentlyResource
import javax.inject.Inject

/**
 * Created on 11.01.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class UnsubscribePushTokenUseCase @Inject constructor(
    private val schedulers: Schedulers,
    private val storage: SharedStorage,
    private val pushApi: PushApi,
    private val profileRepository: ProfileRepository
) : UseCase {

    private var onSuccessCallback: (() -> Unit)? = null

    override fun execute() {
        if (profileRepository.id.isBlank() || storage.siteId.isBlank()) {
            return
        }

        val deviceId = storage.deviceId
        if (deviceId.isBlank()) return

        val deviceInfo = Device(deviceId, token = "")

        schedulers.ui.execute {
            createRequest(deviceInfo).loadSilentlyResource {
                this.error {
                    Jivo.e(Throwable(it), "Request to disable notifications rejected")
                }
                result {
                    onSuccessCallback?.invoke()
                    storage.pushToken = ""
                    storage.hasSentPushToken = false
                }
            }
        }
    }

    fun onSuccess(onSuccessHandler: () -> Unit): UseCase {
        onSuccessCallback = onSuccessHandler
        return this
    }

    private fun createRequest(device: Device): LiveData<Resource<Unit>> {
        return NetworkResource.Builder<Unit, Unit>(schedulers).createCall {
            pushApi.sendDeviceInfo(
                profileRepository.id,
                storage.siteId.toLong(),
                storage.widgetId,
                device
            )
        }.handleResponse {

        }.build().asLiveData()
    }
}