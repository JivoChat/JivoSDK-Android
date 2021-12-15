package com.jivosite.sdk.di.ui.logs

import com.jivosite.sdk.di.ui.UIScope
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.adapters.SimpleDiffAdapter
import com.jivosite.sdk.ui.logs.JivoLogsFragment
import com.jivosite.sdk.ui.logs.items.error.ErrorItemDelegate
import com.jivosite.sdk.ui.logs.items.error.ErrorItemViewModel
import com.jivosite.sdk.ui.logs.items.message.MessageItemDelegate
import com.jivosite.sdk.ui.logs.items.message.MessageItemViewModel
import com.jivosite.sdk.ui.logs.items.system.SystemItemDelegate
import com.jivosite.sdk.ui.logs.items.system.SystemItemViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@Module
class JivoLogsFragmentModule(private val fragment: JivoLogsFragment) {

    @IntoSet
    @Provides
    fun provideMessageItemDelegate(viewModelProvider: Provider<MessageItemViewModel>): AdapterDelegate<LogMessage> {
        return MessageItemDelegate(fragment.viewLifecycleOwner, fragment.viewModel, viewModelProvider)
    }

    @IntoSet
    @Provides
    fun provideSystemItemDelegate(viewModelProvider: Provider<SystemItemViewModel>): AdapterDelegate<LogMessage> {
        return SystemItemDelegate(fragment.viewLifecycleOwner, viewModelProvider)
    }

    @IntoSet
    @Provides
    fun provideErrorItemDelegate(viewModelProvider: Provider<ErrorItemViewModel>): AdapterDelegate<LogMessage> {
        return ErrorItemDelegate(fragment.viewLifecycleOwner, viewModelProvider)
    }

    @UIScope
    @Provides
    fun provideAdapter(delegates: Set<@JvmSuppressWildcards AdapterDelegate<LogMessage>>): SimpleDiffAdapter<LogMessage> {
        return SimpleDiffAdapter<LogMessage>().apply {
            delegates.forEach {
                add(it)
            }
        }
    }
}
