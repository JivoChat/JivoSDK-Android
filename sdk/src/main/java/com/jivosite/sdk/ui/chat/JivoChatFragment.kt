package com.jivosite.sdk.ui.chat

import android.content.*
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.FragmentJivoChatBinding
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.builders.Config
import com.jivosite.sdk.support.dg.adapters.SimpleDiffAdapter
import com.jivosite.sdk.support.recycler.AutoScroller
import com.jivosite.sdk.support.vm.ViewModelFactory
import com.jivosite.sdk.ui.chat.items.ChatEntry
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoChatFragment : Fragment(R.layout.fragment_jivo_chat) {

    companion object {
        const val COUNT_TO_LOAD_NEXT_PAGE = 3
        const val MAX_FILE_SIZE_IN_MB = 10
    }

    @Inject
    lateinit var chatAdapterProvider: Provider<SimpleDiffAdapter<ChatEntry>>
    private lateinit var chatAdapter: SimpleDiffAdapter<ChatEntry>

    private var binder: JivoWebSocketService.JivoWebSocketServiceBinder? = null
    private var webSocketServiceBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            binder = service as JivoWebSocketService.JivoWebSocketServiceBinder
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            binder = null
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<JivoChatViewModel>
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(JivoChatViewModel::class.java)
    }

    private lateinit var contentResultCallback: ActivityResultLauncher<String>

    private lateinit var contentUri: Uri
    private lateinit var contentResolver: ContentResolver

    private lateinit var binding: FragmentJivoChatBinding

    private val autoScroller = AutoScroller()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contentResultCallback = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) handleContent(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Jivo.getChatComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = chatAdapterProvider.get()

        binding = FragmentJivoChatBinding.bind(view).also { binding ->
            binding.fragment = this
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner

            binding.appbar.run {
                background = AppCompatResources.getDrawable(context, Jivo.getConfig().background ?: R.drawable.bg_toolbar)
            }
            binding.toolbar.run {
                navigationIcon = AppCompatResources.getDrawable(context, R.drawable.vic_arrow_24dp)
                navigationIcon?.setTint(
                    ContextCompat.getColor(
                        context,
                        Jivo.getConfig().navigationIconTint ?: R.color.white
                    )
                )
                setNavigationOnClickListener {
                    activity?.finish()
                }
            }

            binding.subtitleView.text = getString(Jivo.getConfig().subtitle ?: R.string.chat_subtitle_placeholder)
            val subtitleTextColor =
                AppCompatResources.getColorStateList(view.context, Jivo.getConfig().subtitleTextColor ?: R.color.white30)
            binding.subtitleView.setTextColor(subtitleTextColor)
            binding.subtitleView.alpha = Jivo.getConfig().subtitleTextColorAlpha ?: 1f

            with(binding.recyclerView) {
                itemAnimator = null
                addItemDecoration(JivoChatDecoration())
                setHasFixedSize(false)
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                adapter = this@JivoChatFragment.chatAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        layoutManager.findLastVisibleItemPosition()
                            .takeIf { it >= layoutManager.itemCount - COUNT_TO_LOAD_NEXT_PAGE }
                            ?.run { viewModel.loadNextPage() }
                    }
                })
                autoScroller.attachToRecyclerView(this)
            }

            binding.inputText.doOnTextChanged { msg, _, _, _ ->
                viewModel.message.value = msg.toString()
                viewModel.clientTyping.setValue(msg.toString())
            }
            binding.inputText.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatResources.getDrawable(context, R.drawable.bg_cursor)?.let { drawable ->
                        val color = AppCompatResources.getColorStateList(context, Jivo.getConfig().outgoingMessageColor.color)
                        drawable.setTintList(color)
                        textCursorDrawable = drawable
                    }
                }
            }

            AppCompatResources.getDrawable(requireContext(), R.drawable.bg_outgoing_message)?.let { drawable ->
                val color = AppCompatResources.getColorStateList(requireContext(), Jivo.getConfig().outgoingMessageColor.color)
                drawable.setTintList(color)
                binding.connectingView.indeterminateDrawable.colorFilter =
                    PorterDuffColorFilter(color.defaultColor, PorterDuff.Mode.SRC_IN)
            }
        }

        viewModel.items.observe(viewLifecycleOwner) {
            chatAdapter.items = it
        }

        viewModel.clientMessage.observe(viewLifecycleOwner) { message ->
            binder?.sendMessage(message)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            val message = when (error) {
                is Error.Network -> error.message
                is Error.UnsupportedType -> requireContext().getString(
                    R.string.message_unsupported_media
                )
                is Error.FileOversize -> requireContext().getString(
                    R.string.media_uploading_too_large,
                    MAX_FILE_SIZE_IN_MB
                )
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.connectionState.observe(viewLifecycleOwner) { state ->
            renderConnectionState(state)
        }

        viewModel.clientTyping.observe(viewLifecycleOwner) {
            viewModel.clientTyping(it)
        }

        binding.banner.isVisible = viewModel.siteId == "1"

        renderSendBtn()
    }

    override fun onStart() {
        super.onStart()
        if (!webSocketServiceBound) {
            Intent(requireContext(), JivoWebSocketService::class.java).also { intent ->
                requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
                webSocketServiceBound = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setVisibility(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        if (webSocketServiceBound) {
            requireContext().unbindService(connection)
            webSocketServiceBound = false
        }
        Jivo.clearChatComponent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScroller.release()
        binding.recyclerView.clearOnScrollListeners()
    }

    fun send() {
        binding.inputText.text.toString().also {
            if (it.isNotBlank() && webSocketServiceBound) {
                binding.inputText.text?.clear()
                viewModel.sendTextMessage(it)
//                viewModel.addTextMessage(it)
//                binder.sendMessage(SocketMessage("atom/me.history"))
            }
        }
    }

    fun retry() {
        binder?.retry()
    }

    fun attach() {
        contentResultCallback.launch("*/*")
    }

    private fun renderSendBtn() {
        viewModel.canSend.observe(viewLifecycleOwner) {
            binding.sendBtn.setImageResource(
                if (it) {
                    when (Jivo.getConfig().outgoingMessageColor) {
                        Config.Color.GREEN -> R.drawable.vic_send_green
                        Config.Color.BLUE -> R.drawable.vic_send_blue
                        Config.Color.GREY -> R.drawable.vic_send_gray
                    }
                } else {
                    R.drawable.vic_send_disabled
                }
            )
        }
    }

    private fun renderConnectionState(state: ConnectionState) {
        binding.connectionState.isVisible = when (state) {
            is ConnectionState.Initial, ConnectionState.Connected, ConnectionState.Stopped -> {
                false
            }
            is ConnectionState.Connecting -> {
                binding.connectingView.isVisible = true
                binding.connectionStateName.setText(R.string.connection_state_connecting)
                binding.connectionRetry.isVisible = false
                true
            }
            is ConnectionState.Disconnected -> {
                binding.connectingView.isVisible = false
                binding.connectionStateName.text = getString(R.string.connection_state_disconnected, state.seconds)
                binding.connectionRetry.isVisible = true
                true
            }
        }
    }

    private fun handleContent(uri: Uri) {
        var fileName = ""
        var fileSize = 0L
        var inputStream: InputStream? = null
        var type = ""

        contentUri = uri
        contentResolver = requireContext().contentResolver
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
            fileSize = cursor.getLong(sizeIndex)

            inputStream = contentResolver.openInputStream(contentUri)
            type = contentResolver.getType(contentUri) ?: ""
        }

        viewModel.uploadFile(inputStream, fileName, type, fileSize, uri.toString())
    }
}
