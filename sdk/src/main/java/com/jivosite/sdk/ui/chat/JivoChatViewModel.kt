package com.jivosite.sdk.ui.chat

import android.os.Handler
import android.os.Looper
import android.webkit.MimeTypeMap
import androidx.lifecycle.*
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.logger.LogsRepository
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.model.pojo.agent.AgentStatus
import com.jivosite.sdk.model.pojo.file.JivoMediaFile
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.FILE_TYPES
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_DOCUMENT
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_IMAGE
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_UNKNOWN
import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.pojo.message.splitIdTimestamp
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.chat.ChatStateRepository
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormState
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.history.HistoryState
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.pending.PendingRepository
import com.jivosite.sdk.model.repository.pending.PendingState
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.rating.RatingFormState
import com.jivosite.sdk.model.repository.rating.RatingRepository
import com.jivosite.sdk.model.repository.rating.RatingState
import com.jivosite.sdk.model.repository.send.SendMessageRepository
import com.jivosite.sdk.model.repository.send.SendMessageState
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.model.repository.unsupported.UnsupportedRepository
import com.jivosite.sdk.model.repository.unsupported.UnsupportedState
import com.jivosite.sdk.model.repository.upload.UploadFilesState
import com.jivosite.sdk.model.repository.upload.UploadRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.support.event.Event
import com.jivosite.sdk.support.ext.getSupportFileType
import com.jivosite.sdk.support.livedata.ClientTypingDebounceLiveData
import com.jivosite.sdk.ui.chat.items.*
import com.jivosite.sdk.ui.chat.items.contacts.ContactFormItem
import com.jivosite.sdk.ui.chat.items.event.EventItem
import com.jivosite.sdk.ui.chat.items.message.file.agent.AgentFileItem
import com.jivosite.sdk.ui.chat.items.message.file.client.ClientFileItem
import com.jivosite.sdk.ui.chat.items.message.image.agent.AgentImageItem
import com.jivosite.sdk.ui.chat.items.message.image.client.ClientImageItem
import com.jivosite.sdk.ui.chat.items.message.offline.OfflineMessageItem
import com.jivosite.sdk.ui.chat.items.message.text.agent.AgentTextItem
import com.jivosite.sdk.ui.chat.items.message.text.client.ClientTextItem
import com.jivosite.sdk.ui.chat.items.message.uploading.file.UploadingFileItem
import com.jivosite.sdk.ui.chat.items.message.uploading.image.UploadingImageItem
import com.jivosite.sdk.ui.chat.items.message.welcome.WelcomeMessageItem
import com.jivosite.sdk.ui.chat.items.rate.RatingItem
import com.jivosite.sdk.ui.chat.items.unsupported.UnsupportedItem
import okhttp3.internal.toLongOrDefault
import java.io.InputStream
import java.util.*
import javax.inject.Inject

/**
 * Created on 15.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class JivoChatViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val profileRepository: ProfileRepository,
    private val connectionStateRepository: ConnectionStateRepository,
    private val chatStateRepository: ChatStateRepository,
    private val historyRepository: HistoryRepository,
    private val paginationRepository: PaginationRepository,
    private val sendMessageRepository: SendMessageRepository,
    private val typingRepository: TypingRepository,
    private val messageTransmitter: Transmitter,
    private val logsRepository: LogsRepository,
    private val uploadRepository: UploadRepository,
    private val storage: SharedStorage,
    private val sdkContext: SdkContext,
    private val pendingRepository: PendingRepository,
    private val contactFormRepository: ContactFormRepository,
    private val unsupportedRepository: UnsupportedRepository,
    private val ratingRepository: RatingRepository,
) : ViewModel() {

    companion object {
        const val WELCOME_TIMEOUT = 1000L
        const val OFFLINE_TIMEOUT = 2000L
        const val MAX_FILE_SIZE = 10
    }

    private val handler = Handler(Looper.getMainLooper())

    private val addWelcomeMessageCallback: Runnable = Runnable {
        messagesState.value = messagesState.value?.copy(hasWelcome = true)
    }
    private val addOfflineMessageCallback: Runnable = Runnable {
        messagesState.value = messagesState.value?.copy(hasOffline = true)
    }

    private val messagesState = MediatorLiveData<MessagesState>().apply {
        value = MessagesState()
        addSource(profileRepository.observableState) { value = value?.copy(myId = it.id) }
        addSource(pendingRepository.observableState) {
            value = value?.copy(pendingState = it)
            if (it.message != null) {
                contactFormRepository.createContactForm(true)
            }
        }
        addSource(contactFormRepository.observableState) {
            value = value?.copy(contactFormState = it)
            if (it.hasSentContactInfo) {
                val currentState = value ?: return@addSource
                currentState.pendingState.message?.let { clientMessage ->
                    sendMessage(clientMessage)
                    pendingRepository.removeMessage()
                }
            }
        }
        addSource(historyRepository.observableState) {
            val currentState = value ?: return@addSource
            value = currentState.copy(historyState = it)
            if (it.messages.isEmpty() && !currentState.hasWelcome && currentState.pendingState.message == null) {
                handler.postDelayed(addWelcomeMessageCallback, WELCOME_TIMEOUT)
            } else {
                handler.removeCallbacks(addWelcomeMessageCallback)
            }
            handleOfflineMessage(currentState)
            if (it.messages.isNotEmpty()) {
                contactFormRepository.createContactForm(it.messages.size == 1)
            }
            if (storage.chatId.isNotBlank() && it.messages.isNotEmpty()) {
                ratingRepository.setChatId(storage.chatId)
            }
        }
        addSource(agentRepository.observableState) { state ->
            handleOfflineMessage(value ?: return@addSource)
            if (state.agents.any { it.status == AgentStatus.Online }) {
                val currentState = value ?: return@addSource
                currentState.pendingState.message?.let { clientMessage ->
                    sendMessage(clientMessage)
                    pendingRepository.removeMessage()
                }
            }
        }
        addSource(sendMessageRepository.observableState) {
            value = value?.copy(sendMessageState = it)
            handleOfflineMessage(value ?: return@addSource)
        }
        addSource(logsRepository.messages) { value = value?.copy(eventMessages = prepareEventMessages(it)) }
        addSource(uploadRepository.observableState) { value = value?.copy(uploadFilesState = it) }
        addSource(unsupportedRepository.observableState) { value = value?.copy(unsupportedState = it) }
        addSource(ratingRepository.observableState) { value = value?.copy(ratingState = it) }
    }

    val items: LiveData<List<ChatItem>> = Transformations.map(messagesState) { handleMessagesState(it) }

    val clientTyping = ClientTypingDebounceLiveData<String>()

    val agentsTyping: LiveData<List<Agent>> = Transformations.map(typingRepository.observableState) { state ->
        state?.agents?.mapNotNull {
            agentRepository.getAgent(it.agentId.toString())
        }
    }

    val message = MutableLiveData<String>().apply { value = "" }

    val attachedJivoMediaFile = MutableLiveData<JivoMediaFile?>()

    val connectionState: LiveData<ConnectionState>
        get() = connectionStateRepository.state

    val agents: LiveData<List<Agent>> = Transformations.map(agentRepository.observableState) {
        it.agents
    }

    val isUnavailable: LiveData<Boolean> = Transformations.map(chatStateRepository.observableState) {
        it.blacklisted || it.sanctioned
    }

    private val _canSendState = MediatorLiveData<CanSendState>().apply {
        value = CanSendState()
        addSource(message) {
            value = value?.copy(hasMessage = !it.isNullOrBlank())
        }
        addSource(connectionState) {
            value = value?.copy(hasConnection = it is ConnectionState.Connected)
        }

        addSource(messagesState) {
            value = value?.copy(hasPendingMessage = it.pendingState.message != null)
        }

        addSource(attachedJivoMediaFile) {
            value = value?.copy(hasAttachedFile = it != null)
        }

    }
    val canSend = Transformations.map(_canSendState) {
        it != null && it.hasMessage && it.hasConnection && !it.hasPendingMessage || it.hasAttachedFile
    }

    val canUploadFile = Transformations.map(uploadRepository.hasLicense) { it }

    private val _isLoading = MutableLiveData<Boolean>()

    private val _errorAttachState = MutableLiveData<Event<ErrorAttachState>>()
    val errorAttachState: LiveData<Event<ErrorAttachState>>
        get() = _errorAttachState

    private val _canAttachState = MediatorLiveData<CanAttachState>().apply {
        value = CanAttachState()
        addSource(_isLoading) {
            value = value?.copy(isLoading = it == true)
        }
        addSource(connectionState) {
            value = value?.copy(hasConnection = it is ConnectionState.Connected)
        }

        addSource(messagesState) {
            value = value?.copy(
                hasPendingMessage = it.pendingState.message != null,
                hasHistoryMessages = it.historyState.messages.isNotEmpty()
            )
        }
    }

    val canInputText = Transformations.map(messagesState) {
        it.pendingState.message == null
    }

    val canAttach = Transformations.map(_canAttachState) {
        it != null && !it.isLoading && it.hasConnection && !it.hasPendingMessage && it.hasHistoryMessages
    }

    val siteId
        get() = storage.siteId

    private fun handleMessagesState(state: MessagesState): List<ChatItem> {
        if (state.myId.isBlank()) {
            return if (state.hasWelcome) {
                listOf(WelcomeMessageItem())
            } else {
                Collections.emptyList()
            }
        }

        val buffer = ArrayList<MessageEntry>(state.size)
        val messages: SortedMap<Long, ChatEntry> = TreeMap<Long, ChatEntry> { o1, o2 ->
            o2.compareTo(o1)
        }.apply {
            state.historyState.messages.forEach {
                if (it.from.toLongOrNull() == null || it.from == state.myId) {
                    putOrIncrementKey(it.timestamp, ClientMessageEntry(it, EntryPosition.Single))
                } else {
                    putOrIncrementKey(
                        it.timestamp, AgentMessageEntry(
                            it, EntryPosition.Single,
                            it.timestamp >= (state.historyState.messages.lastOrNull()?.timestamp ?: 0)
                        )
                    )
                }
            }

            state.sendMessageState.messages.forEach {
                putOrIncrementKey(it.timestamp, SendingMessageEntry(it, EntryPosition.Single))
            }

            state.eventMessages.forEach {
                putOrIncrementKey(it.ts, EventEntry(it.code, it.reason))
            }

            state.uploadFilesState.files.forEach {
                putOrIncrementKey(it.value.timestamp, UploadingFileEntry(it.value, EntryPosition.Single))
            }

            state.pendingState.message?.let {
                putOrIncrementKey(it.timestamp, SendingMessageEntry(it, EntryPosition.Single))
            }

            state.contactFormState.run {
                if (!storage.hasSentContactInfo) {
                    this.contactForm?.let {
                        putOrIncrementKey(
                            state.historyState.messages.firstOrNull()?.timestamp?.inc() ?: this.contactForm.timestamp,
                            ContactFormEntry(this)
                        )
                    }
                }
            }

            state.unsupportedState.messages.forEach {
                putOrIncrementKey(it.id.splitIdTimestamp().second, UnsupportedEntry(it))
            }

            state.ratingState.also { state ->
                if (state.ratingFormState !is RatingFormState.Initial) {
                    putOrIncrementKey(state.timestamp, RatingEntry(state))
                }
            }
        }

        val result = ArrayList<ChatItem>(state.size)

        messages.values.forEach { message ->
            val previousMessage = buffer.lastOrNull()
            when (message) {
                is MessageEntry -> {
                    val from = if (message.from.isBlank()) state.myId else message.from
                    val previousFrom = if (previousMessage?.from.isNullOrBlank()) state.myId else previousMessage?.from

                    if (previousFrom == null) {
                        buffer.add(message)
                    } else {
                        if (from != previousFrom) {
                            dropBuffer(state.myId, buffer, result)
                        }
                        buffer.add(message)
                    }
                }

                is EventEntry -> {
                    dropBuffer(state.myId, buffer, result)
                    result.add(EventItem(message))
                }

                is ContactFormEntry -> {
                    dropBuffer(state.myId, buffer, result)
                    result.add(ContactFormItem(message))
                }

                is UnsupportedEntry -> {
                    dropBuffer(state.myId, buffer, result)
                    result.add(UnsupportedItem(message))
                }

                is RatingEntry -> {
                    dropBuffer(state.myId, buffer, result)
                    result.add(RatingItem(message))
                }
            }
        }
        if (buffer.isNotEmpty()) {
            dropBuffer(state.myId, buffer, result)
        }

        if (state.hasOffline && storage.hasSentContactInfo && result.isNotEmpty()) {
            val agentList = agents.value ?: emptyList()
            if (!agentList.any { it.status == AgentStatus.Online }) {
                val data = result.first().data
                if (data is ClientMessageEntry) {
                    if (data.message.status == MessageStatus.Sent || data.message.status == MessageStatus.Delivered) {
                        result.add(0, OfflineMessageItem())
                    }
                }
            }
        }

        if (state.hasWelcome) {
            result.add(WelcomeMessageItem())
        }

        return result
    }

    private fun dropBuffer(myId: String, buffer: MutableList<MessageEntry>, sink: MutableList<ChatItem>) {
        if (buffer.size == 1) {
            sink.add(createItem(myId, buffer[0]))
        } else {
            buffer.forEachIndexed { index, message ->
                val position = when (index) {
                    0 -> EntryPosition.First
                    buffer.size - 1 -> EntryPosition.Last
                    else -> EntryPosition.Middle
                }
                sink.add(createItem(myId, message.changePosition(position)))
            }
        }
        buffer.clear()
    }

    private fun TreeMap<Long, ChatEntry>.putOrIncrementKey(timestamp: Long, entry: ChatEntry) {
        var hasIncrement = true
        var key = timestamp
        while (hasIncrement) {
            if (this[key] != null) {
                key++
            } else {
                this[key] = entry
                hasIncrement = false
            }
        }
    }

    private fun createItem(myId: String, message: MessageEntry): ChatItem {
        return if (message.from.isBlank() || message.from.toLongOrNull() == null || message.from == myId) {
            if (message is UploadingFileEntry) {
                when (message.state.type) {
                    TYPE_IMAGE -> UploadingImageItem(message)
                    else -> UploadingFileItem(message)
                }
            } else {
                when (message.type.getSupportFileType()) {
                    TYPE_DOCUMENT -> {
                        if (message.data.isFileType()) {
                            ClientFileItem(message)
                        } else {
                            ClientTextItem(message)
                        }
                    }

                    TYPE_IMAGE -> ClientImageItem(message)
                    else -> ClientFileItem(message)
                }
            }
        } else {
            when (message.type.getSupportFileType()) {
                TYPE_DOCUMENT -> {
                    if (message.data.isFileType()) {
                        AgentFileItem(message)
                    } else {
                        AgentTextItem(message)
                    }
                }

                TYPE_IMAGE -> AgentImageItem(message)
                else -> AgentFileItem(message)
            }
        }
    }

    private fun prepareEventMessages(messages: List<LogMessage>): List<LogMessage.Disconnected> {
        val list = mutableListOf<LogMessage.Disconnected>()
        messages.forEach { message ->
            if (message is LogMessage.Disconnected && message.code == 1000) {
                list.add(message)
            }
        }
        return list
    }

    fun prepareJivoMediaFile(
        inputStream: InputStream?,
        fileName: String,
        mimeType: String,
        fileSize: Long,
        contentUri: String,
    ) {

        if (fileSize.toInt() > MAX_FILE_SIZE * 1024 * 1024) {
            _errorAttachState.value = Event(ErrorAttachState.FileOversize)
            return
        }

        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""
        val type = FILE_TYPES[extension] ?: TYPE_UNKNOWN
        if (type == TYPE_UNKNOWN) {
            _errorAttachState.value = Event(ErrorAttachState.UnsupportedType)
            return
        }

        val jivoMediaFile = JivoMediaFile(fileName, type, extension, mimeType, inputStream, contentUri, fileSize)
        attachedJivoMediaFile.value = jivoMediaFile
    }

    private fun handleOfflineMessage(state: MessagesState) {
        if (!state.hasOffline) {
            handler.postDelayed(addOfflineMessageCallback, OFFLINE_TIMEOUT)
        } else {
            handler.removeCallbacks(addOfflineMessageCallback)
            messagesState.value = messagesState.value?.copy(hasOffline = false)
        }
    }

    fun prepareMessage(message: String) {
        attachedJivoMediaFile.value?.let {
            attachedJivoMediaFile.value = null
            uploadRepository.upload(it) { url ->
                uploadRepository.removeFile(it.uri)
                sendMessage(ClientMessage.createFile(it.mimeType, url))
                sendMessage(ClientMessage.createText(message))
            }
        } ?: sendMessage(ClientMessage.createText(message))
    }

    fun sendMessage(message: ClientMessage) {
        if (message.data.isBlank()) return
        val historyMessages = messagesState.value?.historyState?.messages
        if (!storage.hasSentContactInfo && agents.value.isNullOrEmpty() && historyMessages.isNullOrEmpty()) {
            pendingRepository.addMessage(message)
        } else {
            sendMessageRepository.addMessage(message)
            val socketMessage = SocketMessage.fromClientMessage(message)
            messageTransmitter.sendMessage(socketMessage)
        }
    }

    /**
     * Изменение состояния присутствия экрана чата на переднем плане.
     * @param isVisible true - экран на переднем плане.
     */
    fun setVisibility(isVisible: Boolean) {
        chatStateRepository.setVisibility(isVisible)
        if (isVisible) {
            historyRepository.state.let { state ->
                val message = state.messages.lastOrNull()
                if (message != null && message.from != profileRepository.id && message.number != state.lastReadMsgId) {
                    messageTransmitter.sendMessage(SocketMessage.ack(message.id))
                    historyRepository.markAsRead(message.number)
                }
            }
        }
    }

    fun loadNextPage() {
        paginationRepository.state.takeIf { it.hasNextPage && !it.loading }?.run {
            paginationRepository.loadingStarted()
            val msg = historyRepository.state.messages.lastOrNull() ?: return
            messageTransmitter.sendMessage(SocketMessage.history(msg.number))
        }
    }

    fun clientTyping(incompleteText: String) {
        messageTransmitter.sendMessage(SocketMessage.clientTyping(incompleteText, profileRepository.id))
    }

    fun retry() {
        JivoWebSocketService.reconnect(sdkContext.appContext)
    }

    fun cancel() {
        attachedJivoMediaFile.value = null
    }

    private data class CanSendState(
        val hasMessage: Boolean = false,
        val hasConnection: Boolean = false,
        val hasPendingMessage: Boolean = false,
        val hasAttachedFile: Boolean = false,
    )

    private data class CanAttachState(
        val isLoading: Boolean = false,
        val hasConnection: Boolean = false,
        val hasPendingMessage: Boolean = false,
        val hasHistoryMessages: Boolean = false,
    )

    private data class MessagesState(
        val myId: String = "",
        val hasWelcome: Boolean = false,
        val hasOffline: Boolean = false,
        val historyState: HistoryState = HistoryState(),
        val sendMessageState: SendMessageState = SendMessageState(),
        val eventMessages: List<LogMessage.Disconnected> = Collections.emptyList(),
        val uploadFilesState: UploadFilesState = UploadFilesState(),
        val pendingState: PendingState = PendingState(),
        val contactFormState: ContactFormState = ContactFormState(),
        val unsupportedState: UnsupportedState = UnsupportedState(),
        val ratingState: RatingState = RatingState(),
    ) {
        val size: Int
            get() = historyState.messages.size + sendMessageState.messages.size + eventMessages.size + uploadFilesState.files.size + pendingState.size + contactFormState.size + unsupportedState.messages.size + ratingState.size
    }
}

sealed class ErrorAttachState {

    object FileOversize : ErrorAttachState()

    object UnsupportedType : ErrorAttachState()
}
