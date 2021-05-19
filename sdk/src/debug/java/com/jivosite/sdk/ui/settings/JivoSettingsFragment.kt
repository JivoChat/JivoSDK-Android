package com.jivosite.sdk.ui.settings

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.FragmentJivoSettingsBinding
import com.jivosite.sdk.socket.JivoWebSocketService
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

    private lateinit var binder: JivoWebSocketService.JivoWebSocketServiceBinder
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            binder = service as JivoWebSocketService.JivoWebSocketServiceBinder
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
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

//        viewModel.restart.observe(viewLifecycleOwner) {
//            if (it == true && mBound) {
//                binder.restart()
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), JivoWebSocketService::class.java).also { intent ->
            requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connection)
        mBound = false
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