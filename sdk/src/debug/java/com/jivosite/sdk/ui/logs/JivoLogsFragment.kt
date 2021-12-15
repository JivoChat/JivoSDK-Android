package com.jivosite.sdk.ui.logs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.FragmentJivoLogsBinding
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.adapters.SimpleDiffAdapter
import com.jivosite.sdk.support.vm.ViewModelFactory
import com.jivosite.sdk.ui.logs.items.LogsItemDecoration
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoLogsFragment : Fragment(R.layout.fragment_jivo_logs) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<JivoLogsViewModel>
    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(JivoLogsViewModel::class.java)
    }

    @Inject
    lateinit var adapterProvider: Provider<SimpleDiffAdapter<LogMessage>>
    private lateinit var adapter: SimpleDiffAdapter<LogMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Jivo.getLogsComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = adapterProvider.get()

        FragmentJivoLogsBinding.bind(view).also { binding ->
            binding.fragment = this
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner

            binding.recyclerView.run {
                itemAnimator = null
                addItemDecoration(LogsItemDecoration(requireContext()))
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    stackFromEnd = true
                }
                adapter = this@JivoLogsFragment.adapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Jivo.clearLogsComponent()
    }
}