package com.jivosite.sdk.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.FragmentJivoSettingsBinding
import com.jivosite.sdk.support.vm.ViewModelFactory
import javax.inject.Inject

/**
 * Created on 12/9/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoSettingsFragment : Fragment(R.layout.fragment_jivo_settings) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<JivoSettingsViewModel>
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(JivoSettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Jivo.getSettingsComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentJivoSettingsBinding.bind(view).also { binding ->
            binding.fragment = this
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onStop() {
        super.onStop()
        Jivo.clearSettingsComponent()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jivo.clearSettingsComponent()
    }

    fun onDisablePush(isDisable: Boolean) {
        if (isDisable) {
            Jivo.unsubscribeFromPush()
        } else {
            Jivo.subscribeToPush()
        }
    }
}