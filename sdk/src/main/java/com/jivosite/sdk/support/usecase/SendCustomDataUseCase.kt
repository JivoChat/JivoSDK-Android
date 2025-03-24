package com.jivosite.sdk.support.usecase

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.Transmitter
import javax.inject.Inject

/**
 * Created on 19.04.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class SendCustomDataUseCase @Inject constructor(
    private val storage: SharedStorage,
    private val messageTransmitter: Transmitter,
    private val connectionStateRepository: ConnectionStateRepository
) : UseCase {

    override fun execute() {
        if (!storage.hasSentCustomData && storage.customData.isNotBlank() && connectionStateRepository.state.value == ConnectionState.Connected) {
            messageTransmitter.sendMessage(SocketMessage.customData(storage.customData))
            storage.hasSentCustomData = true
            Jivo.i("Custom data sent successfully")
        }
    }
}