package com.jivosite.sdk.model.pojo.socket

import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.socket.handler.delegates.AtomRateDelegate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Created on 12/5/20.
 *
 * Сообщение, передаваемое по сокету.
 *
 * @param type Тип сообщения, обязательное поле.
 * @param data Данные сообщения. Если поле является валидной http(s)-ссылкой, и находится на файлообменнике
 * с доменом 1го уровня как у подключения, данные сообщения загружаются по этой ссылке автоматически, в случае
 * ошибки загрузки отображается ссылка. Также ссылка отображается в случае, когда клиент не способен отобразить
 * в интерфейсе эти загруженные данные.
 * @param id Идентификатор сущности. Если сущность не пользователь и идентификатор содержит точку или длина не
 * 16 символов, то используется переходный идентификатор, состоит из целого положительного числа до точки и после
 * точки записано время unix в секундах. Позже будут использоваться 16-символьные идентификаторы для всех сущностей
 * они уникальны, определяют последовательность создания сущностей и предельно точное время (до микросекунд) и
 * место создания сущности, сравнивать такие идентификаторы можно просто как строки, алгоритм возвращающий время
 * любой точности наверное стоит предусмотреть заранее в реализации.
 * @param context Контекст отправляемого сообщения.
 * @param to Идентификатор получателя сообщения. Используется при отправке сообщения.
 * @param from Идентификатор отправителя сообщения. Используется при приеме сообщения.
 * @param parent Идентификатор родительского сообщения. Используется для формирования произвольных структур
 * сообщений (пока не реализовано на чатсервере).
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@JsonClass(generateAdapter = true)
data class SocketMessage(
    @Json(name = "type")
    val type: String,
    @Json(name = "data")
    val data: String? = null,
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "context")
    val context: String? = null,
    @Json(name = "to")
    val to: String? = null,
    @Json(name = "from")
    val from: String? = null,
    @Json(name = "parent")
    val parent: String? = null
) {

    companion object {

        /**
         * Сообщение отправляемое на сервер для загрузки истории переписки.
         *
         * @param msgId Идентификатор сообщения начиная с которого нужно загрузить историю. Указанный идентификатор
         * прислан не будет. Если идентификатор не указан, то история грузится с самого последнего сообщения.
         */
        fun history(msgId: Long? = null): SocketMessage {
            return SocketMessage("atom/me.history", data = msgId?.toString())
        }

        /**
         * Сообщение о прочитанности.
         * @param msgId Идентификатор последнего прочитанного сообщения.
         */
        fun ack(msgId: String): SocketMessage {
            return SocketMessage("atom/message.ack", data = msgId)
        }

        /**
         * Тестовое сообщение от пользователя.
         * @param message Тест сообщения.
         */
        fun text(message: String): SocketMessage {
            return SocketMessage("text/plain", data = message, context = UUID.randomUUID().toString())
        }

        /**
         * Преобразование клиентского сообщения в сообщение передаваемое по сокету.
         * @param message [ClientMessage]
         */
        fun fromClientMessage(message: ClientMessage): SocketMessage {
            return SocketMessage(message.type, data = message.data, context = message.context)
        }

        /**
         * Сообщение о наборе текста клиентом.
         * @param incompleteText Неполный текст сообшения клиента.
         * @param clientId Идентификатор клиента.
         */
        fun clientTyping(incompleteText: String, clientId: String): SocketMessage {
            return SocketMessage("atom/user.typing", data = incompleteText, id = clientId)
        }

        /**
         * Сообщение передачи контактных данных клиента.
         * @param type Тип сообщения(atom/user.name, atom/user.email, atom/user.phone, atom/user.desc).
         * @param data Данные о клиенте.
         */
        fun contactInfo(type: String, data: String): SocketMessage {
            return SocketMessage(type, data = data)
        }

        /**
         * С помощью этой функции можно передать произвольную дополнительную информацию о клиенте оператору.
         * Информация отображается в информационной панели справа в приложении оператора.
         * @param data Данные о клиенте.
         */
        fun customData(data: String): SocketMessage {
            return SocketMessage("atom/user.custom-data", data = data)
        }

        /**
         * Сообщение об оценке качества обслуживания.
         * @param data Данные об оценке качества обслуживания.
         * @param chatId Идентификатор чата.
         */
        fun rating(data: String, chatId: String): SocketMessage {
            return SocketMessage(AtomRateDelegate.TYPE, data = data, id = chatId)
        }
    }
}