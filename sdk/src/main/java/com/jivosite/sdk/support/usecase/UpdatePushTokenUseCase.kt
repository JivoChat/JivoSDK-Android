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
import com.jivosite.sdk.support.ext.loadSilently
import java.util.*
import javax.inject.Inject

/**
 * Created on 1/18/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class UpdatePushTokenUseCase @Inject constructor(
    private val sdkContext: SdkContext,
    private val schedulers: Schedulers,
    private val storage: SharedStorage,
    private val pushApi: PushApi,
    private val profileRepository: ProfileRepository
) : UseCase {

    private lateinit var widgetId: String

    override fun execute() {
        if (profileRepository.id.isBlank()) {
            return
        }

        val token = storage.pushToken
        val hasSentPushToken = storage.hasSentPushToken
        widgetId = storage.widgetId

        when {
            token.isBlank() && !hasSentPushToken -> {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Jivo.e(Throwable(task.exception), "Fetching FCM registration token failed")
                        return@OnCompleteListener
                    }
                    task.result?.run {
                        storage.pushToken = this
                        prepareData(this)
                        storage.hasSentPushToken = true
                    }
                }).addOnFailureListener {
                    Jivo.e(Throwable(it), "Fetching FCM registration token failed")
                }
            }
            token.isNotBlank() && !hasSentPushToken -> {
                prepareData(token)
                storage.hasSentPushToken = true
            }

            token.isBlank() && hasSentPushToken -> {
                prepareData(token)
                storage.hasSentPushToken = false
            }
        }
    }

    private fun prepareData(token: String) {
        if (storage.deviceId.isBlank()) {
            storage.deviceId = UUID.randomUUID().toString()
        }

        Jivo.w("token = $token")
        val deviceInfo = Device(
            deviceId = storage.deviceId, token = token
        )

        schedulers.ui.execute {
            createRequest(deviceInfo).loadSilently()
        }
    }

    private fun createRequest(device: Device): LiveData<Resource<Any>> {
        return NetworkResource.Builder<Any, Any>(schedulers).createCall {
            pushApi.sendDeviceInfo(
                profileRepository.id,
                storage.siteId.toLong(),
                widgetId.ifBlank { sdkContext.widgetId },
                device
            )
        }.handleResponse { }.build().asLiveData()
    }
}