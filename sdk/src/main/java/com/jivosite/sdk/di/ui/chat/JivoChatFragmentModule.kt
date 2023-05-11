package com.jivosite.sdk.di.ui.chat

import androidx.fragment.app.Fragment
import com.jivosite.sdk.di.ui.UIScope
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.adapters.SimpleDiffAdapter
import com.jivosite.sdk.ui.chat.JivoChatFragment
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.contacts.ContactFormItemDelegate
import com.jivosite.sdk.ui.chat.items.contacts.ContactFormItemViewModel
import com.jivosite.sdk.ui.chat.items.event.EventItemDelegate
import com.jivosite.sdk.ui.chat.items.event.EventItemViewModel
import com.jivosite.sdk.ui.chat.items.message.file.agent.AgentFileItemDelegate
import com.jivosite.sdk.ui.chat.items.message.file.agent.AgentFileItemViewModel
import com.jivosite.sdk.ui.chat.items.message.file.client.ClientFileItemDelegate
import com.jivosite.sdk.ui.chat.items.message.file.client.ClientFileItemViewModel
import com.jivosite.sdk.ui.chat.items.message.image.agent.AgentImageItemDelegate
import com.jivosite.sdk.ui.chat.items.message.image.agent.AgentImageItemViewModel
import com.jivosite.sdk.ui.chat.items.message.image.client.ClientImageItemDelegate
import com.jivosite.sdk.ui.chat.items.message.image.client.ClientImageItemViewModel
import com.jivosite.sdk.ui.chat.items.message.offline.OfflineMessageItemDelegate
import com.jivosite.sdk.ui.chat.items.message.text.agent.AgentTextItemDelegate
import com.jivosite.sdk.ui.chat.items.message.text.agent.AgentTextItemViewModel
import com.jivosite.sdk.ui.chat.items.message.text.client.ClientTextItemDelegate
import com.jivosite.sdk.ui.chat.items.message.text.client.ClientTextItemViewModel
import com.jivosite.sdk.ui.chat.items.message.uploading.file.UploadingFileItemDelegate
import com.jivosite.sdk.ui.chat.items.message.uploading.file.UploadingFileItemViewModel
import com.jivosite.sdk.ui.chat.items.message.uploading.image.UploadingImageItemDelegate
import com.jivosite.sdk.ui.chat.items.message.uploading.image.UploadingImageItemViewModel
import com.jivosite.sdk.ui.chat.items.message.welcome.WelcomeMessageItemDelegate
import com.jivosite.sdk.ui.chat.items.rate.RateItemViewModel
import com.jivosite.sdk.ui.chat.items.rate.RatingItemDelegate
import com.jivosite.sdk.ui.chat.items.unsupported.UnsupportedItemDelegate
import com.jivosite.sdk.ui.chat.items.unsupported.UnsupportedItemViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.noties.markwon.Markwon
import javax.inject.Provider

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@Module
class JivoChatFragmentModule(private val fragment: Fragment) {

    @IntoSet
    @Provides
    fun provideWelcomeMessageItemDelegate(): AdapterDelegate<ChatEntry> {
        return WelcomeMessageItemDelegate()
    }

    @IntoSet
    @Provides
    fun provideUploadingFileItemDelegate(
        viewModelProvider: Provider<UploadingFileItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return UploadingFileItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideUploadingImageItemDelegate(
        viewModelProvider: Provider<UploadingImageItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return UploadingImageItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideClientTextItemDelegate(
        viewModelProvider: Provider<ClientTextItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return ClientTextItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideClientFileItemDelegate(
        viewModelProvider: Provider<ClientFileItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return ClientFileItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideClientImageItemDelegate(
        viewModelProvider: Provider<ClientImageItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return ClientImageItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideAgentTextItemDelegate(
        viewModelProvider: Provider<AgentTextItemViewModel>,
        markwonProvider: Provider<Markwon>
    ): AdapterDelegate<ChatEntry> {
        return AgentTextItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider,
            (fragment as JivoChatFragment).viewModel,
            markwonProvider
        )
    }

    @IntoSet
    @Provides
    fun provideAgentImageItemDelegate(
        viewModelProvider: Provider<AgentImageItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return AgentImageItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideAgentFileItemDelegate(
        viewModelProvider: Provider<AgentFileItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return AgentFileItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideEventItemDelegate(
        viewModelProvider: Provider<EventItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return EventItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider
        )
    }

    @IntoSet
    @Provides
    fun provideOfflineMessageItemDelegate(): AdapterDelegate<ChatEntry> {
        return OfflineMessageItemDelegate()
    }

    @IntoSet
    @Provides
    fun provideContactFormItemDelegate(
        viewModelProvider: Provider<ContactFormItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return ContactFormItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider,
        )
    }

    @IntoSet
    @Provides
    fun provideUnsupportedItemDelegate(
        viewModelProvider: Provider<UnsupportedItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return UnsupportedItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider,
        )
    }

    @IntoSet
    @Provides
    fun provideRatingItemDelegate(
        viewModelProvider: Provider<RateItemViewModel>
    ): AdapterDelegate<ChatEntry> {
        return RatingItemDelegate(
            fragment.viewLifecycleOwner,
            viewModelProvider,
        )
    }

    @UIScope
    @Provides
    fun provideChatAdapter(
        delegates: Set<@JvmSuppressWildcards AdapterDelegate<ChatEntry>>
    ): SimpleDiffAdapter<ChatEntry> {
        return SimpleDiffAdapter<ChatEntry>().apply {
            delegates.forEach {
                add(it)
            }
        }
    }
}
