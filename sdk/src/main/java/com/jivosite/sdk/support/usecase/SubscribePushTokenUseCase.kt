package com.jivosite.sdk.support.usecase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.api.PushApi
import com.jivosite.sdk.model.SdkContext
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
    private val profileRepository: ProfileRepository
) : UseCase {

    override fun execute() {
        if (storage.hasSentPushToken || profileRepository.id.isBlank() || storage.siteId.isBlank()) {
            return
        }

        val token = storage.pushToken

        when {
            token.isBlank() -> {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    task.result?.run {
                        storage.pushToken = this
                        prepareData(this)
                    }
                }).addOnFailureListener {
                    Jivo.e(Throwable(it), "Fetching FCM registration token failed")
                }
            }

            token.isNotBlank() -> {
                prepareData(token)
            }
        }
    }

    private fun prepareData(token: String) {
        if (storage.deviceId.isBlank()) {
            storage.deviceId = UUID.randomUUID().toString()
        }

        val deviceInfo = Device(
            deviceId = storage.deviceId, token = token
        )

        schedulers.ui.execute {
            createRequest(deviceInfo).loadSilentlyResource {
                result {
                    storage.hasSentPushToken = true
                }
            }
        }
    }

    private fun createRequest(device: Device): LiveData<Resource<Unit>> {
        return NetworkResource.Builder<Unit, Unit>(schedulers).createCall {
            pushApi.sendDeviceInfo(
                profileRepository.id,
                storage.siteId.toLong(),
                storage.widgetId,
                device
            )
        }.handleResponse { }.build().asLiveData()
    }
}