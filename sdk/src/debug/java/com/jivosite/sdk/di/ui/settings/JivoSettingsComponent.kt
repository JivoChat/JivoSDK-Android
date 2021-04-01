package com.jivosite.sdk.di.ui.settings

import com.jivosite.sdk.di.ui.UIScope
import com.jivosite.sdk.ui.settings.JivoSettingsFragment
import dagger.Subcomponent

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
@UIScope
@Subcomponent(
    modules = [
        JivoSettingsFragmentModule::class
    ]
)
interface JivoSettingsComponent {
    fun inject(fragment: JivoSettingsFragment)
}