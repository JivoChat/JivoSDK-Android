package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.repository.chat.ChatStateRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.socket.transmitter.Transmitter
import javax.inject.Inject

/**
 * Created on 2/26/21.
 *
 * Пример сообщения для обработки:
 * {"type":"audio/mpeg","data":"https://files.dev.jivosite.com/file-transfer/75613/2021-02-26/d389d8336d81608f0c7f.mp3","id":"1407.1614339974","from":"7"}
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AudioMpegDelegate @Inject constructor(
    chatStateRepository: ChatStateRepository,
    profileRepository: ProfileRepository,
    historyRepository: HistoryRepository,
    paginationRepository: PaginationRepository,
    typingRepository: TypingRepository,
    messageTransmitter: Transmitter
) : UserMessageDelegate(
    chatStateRepository,
    profileRepository,
    historyRepository,
    paginationRepository,
    typingRepository,
    messageTransmitter
) {

    companion object {
        const val TYPE = "audio/mpeg"
    }
}

