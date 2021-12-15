package com.jivosite.sdk.model.pojo.message

import java.util.*

/**
 * Created on 2/2/21.
 *
 * Сообщение от пользователя.
 *
 * @param context Контекст сообщения. Необходимое поле для того, чтобы в дальнейшем определить,
 * какое из сообщений было доставлено на сервер.
 * @param timestamp Время создания сообщения.
 * @param type Тип сообщения.
 * @param data Данные сообщения, например текст для текстового сообщения, или ссылка для изображения.
 * @param status Статус сообщения, может быть "отправляется" или "ошибка". [MessageStatus]
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class ClientMessage(
    val context: String,
    val timestamp: Long,
    val type: String,
    val data: String,
    val status: MessageStatus
) {

    companion object {

        /**
         * Создание сообщения от пользователя.
         *
         * @param type Тип сообщения.
         * @param data Данные сообщения.
         */
        @JvmStatic
        fun create(type: String, data: String): ClientMessage {
            return ClientMessage(
                context = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis() / 1000,
                type = type,
                data = data,
                status = MessageStatus.Sending
            )
        }

        /**
         * Создание текстового сообщения от пользователя.
         *
         * @param text Текстовое сообщение.
         */
        @JvmStatic
        fun createText(text: String): ClientMessage {
            return create("text/plain", text)
        }

        /**
         * Создание медиа сообщения от пользователя.
         *
         * @param type Тип выгружаемого файла
         * @param url Ссылка на выгружаемый файл.
         */
        @JvmStatic
        fun createFile(type: String, url: String): ClientMessage {
            return create(type, url)
        }
    }

    /**
     * Преобразование в сообщение для истории, после того как будет получено сообщение от сервера
     * с идентификатором.
     *
     * @param id Идентификатор сообщения на сервере.
     * @param from Идентификатор пользователя.
     */
    fun toHistoryMessage(id: String, from: String): HistoryMessage {
        return HistoryMessage(
            id = id,
            type = this.type,
            from = from,
            data = this.data,
            status = MessageStatus.Sent
        )
    }

    /**
     * Установка статуса ошибки отправки сообщения.
     */
    fun toError(): ClientMessage {
        return copy(status = MessageStatus.Error)
    }
}