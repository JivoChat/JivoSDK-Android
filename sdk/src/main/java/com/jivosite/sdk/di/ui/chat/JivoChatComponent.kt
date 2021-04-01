package com.jivosite.sdk.di.ui.chat

import com.jivosite.sdk.di.ui.UIScope
import com.jivosite.sdk.ui.chat.JivoChatFragment
import dagger.Subcomponent

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
@UIScope
@Subcomponent(
    modules = [
        JivoChatFragmentModule::class
    ]
)
interface JivoChatComponent {
    fun inject(fragment: JivoChatFragment)
}