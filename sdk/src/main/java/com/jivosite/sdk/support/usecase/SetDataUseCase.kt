package com.jivosite.sdk.support.usecase

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.ext.verifyHostName
import com.jivosite.sdk.support.utils.isExpired
import com.jivosite.sdk.support.utils.parseJivoJwtPayload
import com.jivosite.sdk.support.utils.parseJivoJwtUserId
import com.squareup.moshi.Moshi
import javax.inject.Inject

/**
 * Created on 28.05.2026.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class SetDataUseCase @Inject constructor(
    private val storage: SharedStorage,
    private val moshi: Moshi,
    private val unsubscribePushTokenUseCase: UnsubscribePushTokenUseCase,
    private val clearUseCase: ClearUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
) {

    private val CHANNEL_ID_REGEX = Regex("^[0-9A-Za-z]{10}$")

    fun execute(widgetId: String, userToken: String, host: String) {
        if (!isValidChannelId(widgetId)) {
            Jivo.e("Invalid channel id = $widgetId")
            return
        }

        moshi.parseJivoJwtPayload(userToken)
            .onSuccess { payload ->
                if (payload?.isExpired() ?: false) {
                    Jivo.e("JWT token is expired")
                }
            }
            .onFailure {
                Jivo.e("Не удалось распарсить JWT токен, причина -  $it")
            }

        when {
            storage.widgetId.isBlank() -> {
                if (widgetId.isNotBlank()) {
                    storage.widgetId = widgetId
                }

                if (userToken.isNotBlank()) {
                    storage.userToken = userToken
                }
            }

            storage.widgetId != widgetId && widgetId.isNotBlank() -> {
                when {
                    userToken.isBlank() -> {
                        unsubscribePushTokenUseCase.onUnsubscribePush {
                            val preservedUserToken = storage.userToken
                            clearUseCase.execute()
                            storage.widgetId = widgetId
                            if (preservedUserToken.isNotBlank()) {
                                storage.userToken = preservedUserToken
                            }
                            Jivo.stopSession()
                        }.execute()
                    }

                    userToken.isNotBlank() -> {
                        unsubscribePushTokenUseCase.onUnsubscribePush {
                            clearUseCase.execute()
                            if (widgetId.isNotBlank()) {
                                storage.widgetId = widgetId
                            }
                            storage.userToken = userToken
                            Jivo.stopSession()
                        }.execute()
                    }
                }
            }

            userToken.isNotBlank() -> {
                when {
                    storage.userToken.isBlank() -> {
                        unsubscribePushTokenUseCase.onUnsubscribePush {
                            deleteClientUseCase.execute()
                            storage.userToken = userToken
                            Jivo.stopSession()
                        }.execute()
                    }

                    compareUserToken(userToken, storage.userToken) -> {
                        unsubscribePushTokenUseCase.onUnsubscribePush {
                            deleteClientUseCase.execute()
                            storage.userToken = userToken
                            Jivo.stopSession()
                        }.execute()
                    }
                }
            }
        }

        if (host.verifyHostName()) {
            storage.host = host
        }
    }


    private fun isValidChannelId(id: String): Boolean {
        val trimmed = id.trim()
        return trimmed.isEmpty() || CHANNEL_ID_REGEX.matches(trimmed)
    }

    private fun compareUserToken(new: String, old: String): Boolean {
        val newUserId = moshi.parseJivoJwtUserId(new)
        val oldUserId = moshi.parseJivoJwtUserId(old)
        return newUserId != oldUserId
    }
}
