package com.jivosite.sdk.ui.chat

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.FragmentJivoChatBinding
import com.jivosite.sdk.model.pojo.file.JivoMediaFile
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_AUDIO
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_DOCUMENT
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_IMAGE
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_VIDEO
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.support.dg.adapters.SimpleDiffAdapter
import com.jivosite.sdk.support.event.EventObserver
import com.jivosite.sdk.support.ext.TakePicture
import com.jivosite.sdk.support.recycler.AutoScroller
import com.jivosite.sdk.support.utils.getFileSize
import com.jivosite.sdk.support.vm.ViewModelFactory
import com.jivosite.sdk.ui.chat.items.ChatEntry
import java.io.File
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Provider


/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
open class JivoChatFragment : Fragment(R.layout.fragment_jivo_chat) {

    companion object {
        const val COUNT_TO_LOAD_NEXT_PAGE = 3
        const val MAX_FILE_SIZE_IN_MB = 10
    }

    @Inject
    lateinit var chatAdapterProvider: Provider<SimpleDiffAdapter<ChatEntry>>
    private lateinit var chatAdapter: SimpleDiffAdapter<ChatEntry>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<JivoChatViewModel>
    open val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(JivoChatViewModel::class.java)
    }

    private lateinit var cameraResultCallback: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermissionCallback: ActivityResultLauncher<String>

    private lateinit var contentResultCallback: ActivityResultLauncher<String>
    private lateinit var pushNotificationPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var contentUri: Uri
    private lateinit var contentResolver: ContentResolver

    private lateinit var binding: FragmentJivoChatBinding

    private val autoScroller = AutoScroller()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        cameraResultCallback = registerForActivityResult(TakePicture()) { result ->
            if (result != null) handleContent(result)
        }

        requestCameraPermissionCallback = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) openCamera()
        }

        contentResultCallback = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) handleContent(result)
        }

        if (Build.VERSION.SDK_INT >= 33) {
            pushNotificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
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
            binding.toolbar.run {
                setNavigationOnClickListener {
                    Jivo.getConfig().onBackPressedCallback?.invoke(this@JivoChatFragment) ?: activity?.onBackPressed()
                }
            }

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
        }

        viewModel.items.observe(viewLifecycleOwner) {
            chatAdapter.items = it
        }

        viewModel.errorAttachState.observe(viewLifecycleOwner, EventObserver { error ->
            val message = when (error) {
                is ErrorAttachState.UnsupportedType -> requireContext().getString(
                    R.string.message_unsupported_media
                )

                is ErrorAttachState.FileOversize -> requireContext().getString(
                    R.string.media_uploading_too_large,
                    MAX_FILE_SIZE_IN_MB
                )
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        viewModel.connectionState.observe(viewLifecycleOwner) { state ->
            renderConnectionState(state)
        }

        viewModel.clientTyping.observe(viewLifecycleOwner) {
            viewModel.clientTyping(it)
        }

        binding.banner.isVisible = viewModel.siteId == "1"

        viewModel.attachedJivoMediaFile.observe(viewLifecycleOwner) {
            renderAttachedFile(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setVisibility(true)
        Jivo.startSession()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        Jivo.clearChatComponent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScroller.release()
        binding.recyclerView.clearOnScrollListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jivo.clearChatComponent()
    }

    fun send() {
        binding.inputText.text.toString().also {
            binding.inputText.text?.clear()
            viewModel.prepareMessage(it)
        }

        if (Build.VERSION.SDK_INT >= 33 && !Jivo.isPermissionGranted(
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        ) {
            pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun renderConnectionState(state: ConnectionState) {
        binding.connectionState.isVisible = when (state) {
            is ConnectionState.Initial, ConnectionState.Connected, ConnectionState.Stopped -> {
                false
            }

            is ConnectionState.LoadConfig, ConnectionState.Connecting -> {
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

            is ConnectionState.Error -> {
                binding.connectingView.isVisible = false
                binding.connectionStateName.text = getString(R.string.connection_state_disconnected, state.seconds)
                binding.connectionRetry.isVisible = true
                true
            }
        }
    }

    fun openMenuActions() {
        PopupMenu(requireContext(), binding.menuActions, Gravity.START).apply {
            inflate(R.menu.menu_chat_input_actions)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_photo -> {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            openCamera()
                        } else {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                requestCameraPermissionCallback.launch(Manifest.permission.CAMERA)
                            } else {
                                showCameraDialogNecessary()
                            }
                        }
                        true
                    }

                    R.id.action_file -> {
                        contentResultCallback.launch("*/*")
                        true
                    }

                    else -> true
                }
            }
            show()
        }
    }

    private fun openCamera() {
        getOutputMediaFileUri()?.let {
            cameraResultCallback.launch(it)
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

        viewModel.prepareJivoMediaFile(inputStream, fileName, type, fileSize, uri.toString())
    }

    private fun getOutputMediaFileUri(): Uri? {
        return context?.run {
            FileProvider.getUriForFile(this, "${this.packageName}.jivosdk.fileprovider", getOutputMediaFile()!!)
        }
    }

    private fun getOutputMediaFile(): File {
        return createTempFile(
            "IMG_${System.currentTimeMillis()}",
            ".jpg",
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }

    private fun renderAttachedFile(attachedJivoMediaFile: JivoMediaFile?) {
        binding.run {
            if (attachedJivoMediaFile != null) {
                attachedFile.isVisible = true
                icon.run {
                    when (attachedJivoMediaFile.type) {
                        TYPE_DOCUMENT -> renderIcon(R.drawable.jivo_sdk_vic_file)
                        TYPE_IMAGE -> {
                            setBackgroundResource(0)
                            setPadding(0)
                            imageTintList = null
                            load(attachedJivoMediaFile.uri)
                        }

                        TYPE_VIDEO -> renderIcon(R.drawable.jivo_sdk_vic_video)
                        TYPE_AUDIO -> renderIcon(R.drawable.jivo_sdk_vic_audio)
                        else -> renderIcon(R.drawable.jivo_sdk_vic_file)
                    }
                }
                fileSize.text = getFileSize(
                    requireContext(), attachedJivoMediaFile.size
                )
                fileName.text = attachedJivoMediaFile.name
            } else {
                attachedFile.isVisible = false
            }
        }
    }

    private fun AppCompatImageView.renderIcon(@DrawableRes drawableResId: Int) {
        this.run {
            setBackgroundResource(R.drawable.jivo_sdk_bg_attached_icon)
            imageTintList =
                ColorStateList.valueOf(MaterialColors.getColor(this, R.attr.colorOnPrimary))
            load(drawableResId)
        }
    }

    private fun showCameraDialogNecessary() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme).apply {
            setCancelable(true)
            setMessage(R.string.Camera_Access_Description)
            setPositiveButton(R.string.Common_Settings) { _, _ ->
                Intent().apply {
                    setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    setData(Uri.fromParts("package", requireContext().packageName, null))
                    startActivity(this)
                }
            }
            setNegativeButton(R.string.common_cancel) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    R.string.Camera_Access_Restricted,
                    Toast.LENGTH_LONG
                ).show()
            }
            show()
        }
    }

}
