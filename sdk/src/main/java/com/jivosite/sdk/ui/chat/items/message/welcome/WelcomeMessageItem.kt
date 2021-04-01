package com.jivosite.sdk.ui.chat.items.message.welcome

import com.jivosite.sdk.ui.chat.items.ChatItem
import com.jivosite.sdk.ui.chat.items.WelcomeMessageEntry

/**
 * Created on 30.11.2020.
 *
 * Приветственное сообщение. Отображается, если история сообщений пустая.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class WelcomeMessageItem : ChatItem(VT_WELCOME, WelcomeMessageEntry)