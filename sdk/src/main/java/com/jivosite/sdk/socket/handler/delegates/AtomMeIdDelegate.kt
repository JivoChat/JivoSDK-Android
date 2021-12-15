package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import com.jivosite.sdk.support.usecase.UpdatePushTokenUseCase
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created on 12/5/20.
 *
 * Пример сообщения для обработки:
 * {"type":"atom/me.id","data":"211.gnEVRL77+NxHERLl2+B8nBPby0+2FunyAuT0Qic1hCU"}
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomMeIdDelegate @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val updatePushTokenUseCaseProvider: Provider<UpdatePushTokenUseCase>
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/me.id"
    }

    override fun handle(message: SocketMessage) {
        message.data?.run {
            profileRepository.setId(this)
            updatePushTokenUseCaseProvider.get().execute()
        }
    }
}