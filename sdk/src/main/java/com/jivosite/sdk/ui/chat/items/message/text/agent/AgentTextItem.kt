package com.jivosite.sdk.ui.chat.items.message.text.agent

import com.jivosite.sdk.ui.chat.items.ChatItem
import com.jivosite.sdk.ui.chat.items.MessageEntry

/**
 * Created on 16.09.2020.
 *
 * Элемент списка, описывающий сообщение от агента.
 * @param message Информация о сообщении.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentTextItem(message: MessageEntry) : ChatItem(VT_AGENT_TEXT, message)