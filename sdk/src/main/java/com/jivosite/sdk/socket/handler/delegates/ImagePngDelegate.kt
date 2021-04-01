package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.repository.chat.ChatStateRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.socket.transmitter.Transmitter
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Пример сообщения с изображением:
 * {"type":"image/png","data":"https://files.dev.jivosite.com/file-transfer/75613/2021-02-07/5c611f0ecfd631f0001f.png","id":"1022.1612693586","from":"6"}
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class ImagePngDelegate @Inject constructor(
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
        const val TYPE = "image/png"
    }
}