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
import java.util.UUID
import javax.inject.Inject

/**
 * Created on 11.01.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class SubscribePushTokenUseCase @Inject constructor(
    private val schedulers: Schedulers,
    private val storage: SharedStorage,
    private val pushApi: PushApi,
    private val profileRepository: ProfileRepository,
) : UseCase {

    private lateinit var clientId: String
    private lateinit var siteId: String
    private lateinit var deviceId: String
    private lateinit var token: String
    private lateinit var pushProvider: String


    override fun execute() {
        deviceId = storage.deviceId.ifBlank { UUID.randomUUID().toString() }.also { storage.deviceId = it }
        clientId = profileRepository.id
        siteId = storage.siteId
        token = storage.pushToken
        pushProvider = storage.pushProvider

        if (storage.hasSentPushToken) {
            return
        } else if (clientId.isBlank() || siteId.isBlank() || token.isBlank() || pushProvider.isBlank()) {
            Jivo.e("Failed to subscribe to push notifications due to missing required parameters: clientId = $clientId,  siteId = $siteId, token = $token, pushProvider = $pushProvider")
            return
        }

        prepareData(token, pushProvider)
    }

    private fun prepareData(token: String, pushProvider: String) {

        val deviceInfo = Device(
            deviceId = deviceId, token = token, pushProvider = pushProvider
        )

        schedulers.ui.execute {
            createRequest(clientId, siteId.toLong(), storage.widgetId, deviceInfo).loadSilentlyResource {
                result {
                    Jivo.i("Successful request to subscribe to push notifications")
                    storage.hasSentPushToken = true
                }
                error {
                    Jivo.e("An unsuccessful request to send device info, error - $it")
                }
            }
        }
    }

    private fun createRequest(clientId: String, siteId: Long, widgetId: String, device: Device): LiveData<Resource<Unit>> {
        Jivo.i("Create request to subscribe to push notifications, parameters: clientId = $clientId, siteId = $siteId, widgetId = $widgetId, device = ${device.deviceId}")
        return NetworkResource.Builder<Unit, Unit>(schedulers).createCall {
            pushApi.sendDeviceInfo(
                clientId,
                siteId,
                widgetId,
                device
            )
        }.handleResponse { }.build().asLiveData()
    }
}