package com.jivosite.sdk.model.pojo.message

import com.jivosite.sdk.model.pojo.socket.SocketMessage

/**
 * Created on 12/14/20.
 *
 * Сообщение, которое находится в истории. То есть информацию о нем есть на сервере.
 *
 * @param id Идентификатор сообщения.
 * @param type Тип сообщения.
 * @param from Идентификатор отправителя сообщения.
 * @param data Полезная нагрузка, данные сообщения.
 * @param status [MessageStatus].
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
data class HistoryMessage(
    val id: String,
    val type: String,
    val from: String,
    val data: String,
    val status: MessageStatus
) {

    companion object {

        private const val CUSTOM_TO_STRING = true

        /**
         * Создает пустое сообщение с неизвестным статусом.
         */
        fun empty(): HistoryMessage = HistoryMessage("", "", "", "", MessageStatus.Unknown)

        /**
         * Преобразует сообщение от сервера, полученное через веб сокет, в сообщение истории для отображения
         * пользователю.
         */
        fun mapFrom(message: SocketMessage): HistoryMessage {
            return HistoryMessage(
                id = message.id ?: "",
                type = message.type,
                from = message.from ?: "",
                data = message.data ?: "",
                status = MessageStatus.Delivered
            )
        }
    }

    val number: Long
    val timestamp: Long
    val msgId: Long

    init {
        id.splitIdTimestamp().also {
            number = it.first
            timestamp = it.second
            msgId = it.first + it.second
        }
    }

    override fun toString(): String {
        return if (CUSTOM_TO_STRING) {
            """HistoryMessage{$number-"$data"}"""
        } else {
            super.toString()
        }
    }
}

/**
 * Метод для разделения строки идентификатора сообщения на номерной идентификатор и время(timestamp).
 */
fun String?.splitIdTimestamp(): Pair<Long, Long> {
    return if (isNullOrBlank()) {
        0L to 0L
    } else {
        val idParts = split(".")
        if (idParts.size > 1) {
            (idParts[0].toLongOrNull() ?: 0L) to (idParts[1].toLongOrNull() ?: 0L)
        } else {
            (idParts[0].toLongOrNull() ?: 0L) to 0L
        }
    }
}


/**
 * Статус сообщения.
 * Не известен, отправляется, отправлено, доставлено и ошибка отправки.
 */
sealed class MessageStatus {

    /**
     * Статус сообщения не известен.
     */
    object Unknown : MessageStatus() {
        override fun toString() = "UNKNOWN"
    }

    /**
     * Сообщение отправлется.
     */
    object Sending : MessageStatus() {
        override fun toString() = "SENDING"
    }

    /**
     * Сообщение отправлено на сервер.
     */
    object Sent : MessageStatus() {
        override fun toString() = "SENT"
    }

    /**
     * Сообщение доставлено до адресата.
     */
    object Delivered : MessageStatus() {
        override fun toString() = "DELIVERED"
    }

    /**
     * Сообщение не отправлено.
     */
    object Error : MessageStatus() {
        override fun toString() = "ERROR"
    }
}
