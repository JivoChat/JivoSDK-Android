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

    private lateinit var clientId: String
    private lateinit var siteId: String

    private var onSuccessCallback: (() -> Unit)? = null

    override fun execute() {
        clientId = profileRepository.id
        siteId = storage.siteId

        if (clientId.isBlank() || siteId.isBlank()) {
            Jivo.e("Failed to unsubscribe to push notifications due to missing required parameters: clientId = $clientId,  siteId = $siteId")
            return
        }

        val deviceId = storage.deviceId
        if (deviceId.isBlank()) return

        val deviceInfo = Device(deviceId, token = "")

        schedulers.ui.execute {
            createRequest(clientId, siteId.toLong(), storage.widgetId, deviceInfo).loadSilentlyResource {
                result {
                    Jivo.i("Successful request to unsubscribe to push notifications")
                    onSuccessCallback?.invoke()
                    storage.pushToken = ""
                    storage.hasSentPushToken = false
                }
                error {
                    Jivo.e("An unsuccessful request to unsubscribe to push notifications, error - $it")
                }
            }
        }
    }

    fun onSuccess(onSuccessHandler: () -> Unit): UseCase {
        onSuccessCallback = onSuccessHandler
        return this
    }

    private fun createRequest(clientId: String, siteId: Long, widgetId: String, device: Device): LiveData<Resource<Unit>> {
        Jivo.i("Create request to unsubscribe to push notifications, parameters: clientId = $clientId, siteId = $siteId, widgetId = $widgetId, device = ${device.deviceId}")
        return NetworkResource.Builder<Unit, Unit>(schedulers).createCall {
            pushApi.sendDeviceInfo(
                clientId,
                siteId,
                widgetId,
                device
            )
        }.handleResponse {

        }.build().asLiveData()
    }
}