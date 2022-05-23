package com.jivosite.sdk.ui.chat.items

import android.net.Uri
import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.model.repository.contacts.ContactFormState
import com.jivosite.sdk.model.repository.upload.FileState
import com.jivosite.sdk.support.dg.AdapterDelegateItem

/**
 * Created on 23.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
open class ChatItem(viewType: Int, data: ChatEntry) : AdapterDelegateItem<ChatEntry>(viewType, data) {

    companion object {
        const val VT_WELCOME = VT_ITEM
        const val VT_EVENT = VT_ITEM + 1

        const val VT_CLIENT_TEXT = VT_ITEM + 2
        const val VT_CLIENT_IMAGE = VT_ITEM + 3

        const val VT_AGENT_TEXT = VT_ITEM + 4
        const val VT_AGENT_IMAGE = VT_ITEM + 5

        const val VT_UPLOADING_IMAGE = VT_ITEM + 6
        const val VT_UPLOADING_FILE = VT_ITEM + 7

        const val VT_AGENT_FILE_ITEM = VT_ITEM + 8
        const val VT_CLIENT_FILE_ITEM = VT_ITEM + 9

        const val VT_OFFLINE = VT_ITEM + 10

        const val VT_CONTACT_FORM = VT_ITEM + 11
    }
}

/**
 * Created on 12/17/20.
 *
 * Класс для передачи данных в элемент списка.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */

sealed class ChatEntry

data class EventEntry(val code: Int, val reason: String) : ChatEntry()

sealed class MessageEntry : ChatEntry() {

    val type: String
        get() = when (this) {
            is AgentMessageEntry -> message.type
            is ClientMessageEntry -> message.type
            is SendingMessageEntry -> message.type
            else -> ""
        }

    val from: String
        get() = when (this) {
            is AgentMessageEntry -> message.from
            is ClientMessageEntry -> message.from
            else -> ""
        }

    val data: String
        get() = when (this) {
            is AgentMessageEntry -> message.data
            is ClientMessageEntry -> message.data
            is SendingMessageEntry -> message.data
            else -> ""
        }

    val time: Long
        get() = when (this) {
            is AgentMessageEntry -> message.timestamp
            is ClientMessageEntry -> message.timestamp
            is SendingMessageEntry -> message.timestamp
            is UploadingFileEntry -> state.timestamp
            else -> System.currentTimeMillis() / 1000
        }

    abstract val position: EntryPosition

    fun changePosition(position: EntryPosition): MessageEntry = when (this) {
        is AgentMessageEntry -> copy(position = position)
        is ClientMessageEntry -> copy(position = position)
        is SendingMessageEntry -> copy(position = position)
        is UploadingFileEntry -> copy(position = position)
        is WelcomeMessageEntry -> this
        is OfflineMessageEntry -> this
    }
}

object WelcomeMessageEntry : MessageEntry() {
    override val position: EntryPosition
        get() = EntryPosition.Single
}

object OfflineMessageEntry : MessageEntry() {
    override val position: EntryPosition
        get() = EntryPosition.Single
}

sealed class HistoryMessageEntry : MessageEntry()

data class AgentMessageEntry(val message: HistoryMessage, override val position: EntryPosition) : HistoryMessageEntry()
data class ClientMessageEntry(val message: HistoryMessage, override val position: EntryPosition) : HistoryMessageEntry()

data class SendingMessageEntry(val message: ClientMessage, override val position: EntryPosition) : MessageEntry()

data class UploadingFileEntry(val state: FileState, override val position: EntryPosition) : MessageEntry()

data class ContactFormEntry(val state: ContactFormState) : ChatEntry()

sealed class EntryPosition {
    object First : EntryPosition()
    object Middle : EntryPosition()
    object Last : EntryPosition()
    object Single : EntryPosition()
}

fun String.isFileType(): Boolean {
    return Uri.parse(this).host?.endsWith("jivosite.com", true) ?: false
}


