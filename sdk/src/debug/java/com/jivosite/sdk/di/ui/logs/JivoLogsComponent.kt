package com.jivosite.sdk.di.ui.logs

import com.jivosite.sdk.di.ui.UIScope
import com.jivosite.sdk.ui.logs.JivoLogsFragment
import dagger.Subcomponent

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
@UIScope
@Subcomponent(
    modules = [
        JivoLogsFragmentModule::class
    ]
)
interface JivoLogsComponent {
    fun inject(fragment: JivoLogsFragment)
}