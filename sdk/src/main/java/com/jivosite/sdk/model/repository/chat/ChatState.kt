package com.jivosite.sdk.model.repository.chat

/**
 * Created on 1/27/21.
 *
 * Состояние окна чата.
 *
 * @param visible Наличие окна чата на переднем плане. Влияет на прочитанность сообщений и отображение уведомлений.
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class ChatState(
    val visible: Boolean = false
)
