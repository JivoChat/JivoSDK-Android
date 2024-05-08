package com.jivosite.sdk.support.usecase

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.logger.LogsRepository
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.pending.PendingRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.rating.RatingRepository
import com.jivosite.sdk.model.repository.send.SendMessageRepository
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.model.repository.upload.UploadRepository
import com.jivosite.sdk.model.storage.SharedStorage
import javax.inject.Inject

/**
 * Created on 08.05.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class DeleteClientUseCase @Inject constructor(
    private val storage: SharedStorage,
    private val agentRepository: AgentRepository,
    private val historyRepository: HistoryRepository,
    private val paginationRepository: PaginationRepository,
    private val profileRepository: ProfileRepository,
    private val sendMessageRepository: SendMessageRepository,
    private val typingRepository: TypingRepository,
    private val uploadRepository: UploadRepository,
    private val pendingRepository: PendingRepository,
    private val contactFormRepository: ContactFormRepository,
    private val ratingRepository: RatingRepository,
    private val logsRepository: LogsRepository
) : UseCase {

    override fun execute() {
        storage.run {
            lastReadMsgId = 0
            lastUnreadMsgId = 0
            lastAckMsgId = 0
        }
        agentRepository.clear()
        historyRepository.clear()
        paginationRepository.clear()
        profileRepository.clear()
        sendMessageRepository.clear()
        typingRepository.clear()
        uploadRepository.clear()
        pendingRepository.clear()
        contactFormRepository.clear()
        ratingRepository.clear()
        logsRepository.clear()
        Jivo.i("Delete client has completed")
    }

}