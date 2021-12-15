package com.jivosite.sdk.ui.chat.items.event

import com.jivosite.sdk.ui.chat.items.ChatItem
import com.jivosite.sdk.ui.chat.items.EventEntry

/**
 * Created on 2/9/21.
 *
 * Элемент списка, описывающий событие.
 *
 * @param event Событие от сервера.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class EventItem(event: EventEntry) : ChatItem(VT_EVENT, event)