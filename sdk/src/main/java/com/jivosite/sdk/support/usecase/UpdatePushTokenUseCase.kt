package com.jivosite.sdk.support.usecase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.api.PushApi
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.push.Device
import com.jivosite.sdk.model.pojo.response.Response
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
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class UpdatePushTokenUseCase @Inject constructor(
    private val sdkContext: SdkContext,
    private val schedulers: Schedulers,
    private val storage: SharedStorage,
    private val pushApi: PushApi,
    private val profileRepository: ProfileRepository
) : UseCase {

    override fun execute() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Jivo.e(Throwable(task.exception), "Fetching FCM registration token failed")
                    return@OnCompleteListener
                }
                task.result?.run { execute(this) }
            })
            .addOnFailureListener {
                Jivo.e(Throwable(it), "Fetching FCM registration token failed")
            }
    }

    fun execute(token: String) {
        if (profileRepository.id.isBlank()) {
            Jivo.w("Can not update push token without client id")
            return
        }

        if (storage.deviceId.isBlank()) {
            storage.deviceId = UUID.randomUUID().toString()
        }

        val deviceInfo = Device(
            deviceId = storage.deviceId,
            token = token
        )
        createRequest(deviceInfo).loadSilently()
    }

    private fun createRequest(device: Device): LiveData<Resource<Boolean>> {
        return NetworkResource.Builder<Boolean, Response>(schedulers)
            .createCall {
                pushApi.setPushToken(
                    profileRepository.id,
                    storage.siteId.toLong(),
                    storage.widgetId.ifBlank { sdkContext.widgetId },
                    device
                )
            }
            .handleResponse { it.isOk }
            .build()
            .asLiveData()
    }
}