package com.jivosite.sdk.support.usecase

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.ext.verifyHostName
import com.jivosite.sdk.support.utils.isExpired
import com.jivosite.sdk.support.utils.parseJivoJwtPayload
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

    fun execute(widgetId: String, userToken: String, host: String) {
        val effectiveWidgetId = resolveWidgetId(widgetId) ?: return

        if (userToken.isNotBlank()) {
            logIncomingTokenIssues(userToken)
        }

        when {
            storage.widgetId.isBlank() -> initStorage(effectiveWidgetId, userToken)
            storage.widgetId != effectiveWidgetId -> changeWidget(effectiveWidgetId, userToken)
            userToken.isNotBlank() && shouldReplaceUserToken(userToken, storage.userToken) ->
                replaceUserToken(userToken)
        }

        if (host.verifyHostName()) {
            storage.host = host
        }
    }

    private fun initStorage(widgetId: String, userToken: String) {
        storage.widgetId = widgetId
        if (userToken.isNotBlank()) {
            storage.userToken = userToken
        }
    }

    private fun changeWidget(widgetId: String, userToken: String) {
        resetSession {
            val preservedToken = storage.userToken
            clearUseCase.execute()
            storage.widgetId = widgetId
            storage.userToken = when {
                userToken.isNotBlank() -> userToken
                preservedToken.isNotBlank() -> preservedToken
                else -> ""
            }
        }
    }

    private fun replaceUserToken(userToken: String) {
        resetSession {
            deleteClientUseCase.execute()
            storage.userToken = userToken
        }
    }

    private fun shouldReplaceUserToken(incomingUserToken: String, storedUserToken: String): Boolean {
        if (storedUserToken.isBlank()) {
            return true
        }

        val incoming = parseToken(incomingUserToken)
        val stored = parseToken(storedUserToken)

        if (stored.isExpired && !incoming.isExpired) {
            return true
        }

        return incoming.userId != stored.userId
    }

    private fun parseToken(raw: String): ParsedToken {
        val payload = moshi.parseJivoJwtPayload(raw).getOrNull()
        return ParsedToken(
            userId = payload?.id?.takeIf { it.isNotBlank() },
            isExpired = payload?.isExpired() == true,
        )
    }

    private fun logIncomingTokenIssues(userToken: String) {
        moshi.parseJivoJwtPayload(userToken)
            .onSuccess { payload ->
                if (payload?.isExpired() == true) {
                    Jivo.e("User token is expired")
                }
            }
            .onFailure {
                Jivo.e("Failed to parse User token, reason: $it")
            }
    }

    private fun resetSession(onUnsubscribed: () -> Unit) {
        unsubscribePushTokenUseCase.onUnsubscribePush {
            onUnsubscribed()
            Jivo.stopSession()
        }.execute()
    }

    private fun resolveWidgetId(widgetId: String): String? {
        val trimmed = widgetId.trim()
        if (trimmed.isNotBlank()) {
            if (!isValidWidgetId(trimmed)) {
                Jivo.e("Invalid widgetIdId = $trimmed")
                return null
            }
            return trimmed
        }

        val stored = storage.widgetId.trim()
        if (stored.isNotBlank() && isValidWidgetId(stored)) {
            return stored
        }

        Jivo.e("widgetId is required on first call")
        return null
    }

    private fun isValidWidgetId(id: String): Boolean =
        WIDGET_ID_PATTERN.matches(id)

    private data class ParsedToken(
        val userId: String?,
        val isExpired: Boolean,
    )

    private companion object {
        val WIDGET_ID_PATTERN = Regex("^[0-9A-Za-z]{10}$")
    }
}
