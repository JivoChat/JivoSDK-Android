package com.jivosite.sdk.support.binding

import android.content.Context
import android.graphics.Paint
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.jivosite.sdk.R
import com.jivosite.sdk.api.ApiErrors.FILE_TRANSFER_DISABLED
import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.model.pojo.agent.AgentStatus
import com.jivosite.sdk.model.pojo.agent.isBot
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_AUDIO
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_DOCUMENT
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_IMAGE
import com.jivosite.sdk.model.pojo.file.SupportFileTypes.Companion.TYPE_VIDEO
import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.repository.media.MediaItemState
import com.jivosite.sdk.model.repository.upload.FileState
import com.jivosite.sdk.model.repository.upload.UploadState
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.adapters.BaseAdapter
import com.jivosite.sdk.support.ext.cutName
import com.jivosite.sdk.support.ext.dp
import com.jivosite.sdk.support.ext.getFileType
import java.util.*
import kotlin.math.pow

/**
 * Created on 22.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@BindingAdapter("agent")
fun loadAvatar(view: AppCompatImageView, agent: Agent?) {
    if (agent != null) {
        view.load(agent.photo) {
            placeholder(R.drawable.jivo_sdk_vic_avatar_empty)
            error(if (agent.isBot()) R.drawable.jivo_sdk_ic_avatar_bot else R.drawable.jivo_sdk_vic_avatar_empty)
            transformations(CircleCropTransformation())
        }
    } else {
        view.setImageResource(R.drawable.jivo_sdk_vic_avatar_empty)
    }
}

@BindingAdapter("uploadState")
fun setUploadState(view: AppCompatTextView, state: FileState?) {
    val context = view.context
    val uploadingState = state?.uploadState ?: return
    when (uploadingState) {
        is UploadState.Uploading -> {
            view.isClickable = false
            view.isFocusable = false
            view.text = "${getFileSize(context, uploadingState.size)} / ${getFileSize(context, state.size)}"
        }
        is UploadState.Error -> {
            view.isClickable = false
            view.isFocusable = false
            view.text = context.getString(
                when (uploadingState.errorMessage) {
                    FILE_TRANSFER_DISABLED -> R.string.file_transfer_disabled
                    else -> R.string.media_uploading_common_error
                }
            )
        }
    }
}

private fun getFileSize(context: Context, size: Long) = when (size) {
    in 0..999 -> context.getString(R.string.format_file_size_b, size.toDouble())
    in 1000..999999 -> context.getString(R.string.format_file_size_kb, size.div(1000.0))
    else -> context.getString(R.string.format_file_size_mb, size.div(10.0.pow(6)))
}

@BindingAdapter("items")
fun <T> setItems(view: RecyclerView, items: List<AdapterDelegateItem<T>>?) {
    if (view.adapter is BaseAdapter<*>) {
        (view.adapter as BaseAdapter<T>).items = items ?: Collections.emptyList()
    }
}

@BindingAdapter("messageStatus")
fun setMessageStatus(view: AppCompatImageView, status: MessageStatus?) {
    val (isVisible, imageResId) = when (status) {
        is MessageStatus.Sending -> true to R.drawable.jivo_sdk_vic_message_status_sending
        is MessageStatus.Sent -> true to R.drawable.jivo_sdk_vic_message_status_sent
        is MessageStatus.Delivered -> true to R.drawable.jivo_sdk_vic_message_status_delivered
        is MessageStatus.Error -> true to R.drawable.jivo_sdk_vic_message_status_error
        else -> false to 0
    }
    view.setImageResource(imageResId)
    view.isInvisible = !isVisible
}

@BindingAdapter("typing")
fun setAgentsTyping(view: AppCompatTextView, agents: List<Agent>?) {
    view.isInvisible = agents.isNullOrEmpty()
    if (agents.isNullOrEmpty()) return
    val context = view.context
    view.text = when (agents.size) {
        1 -> context.getString(R.string.chat_system_one_agent_typing, agents[0].name)
        2 -> context.getString(R.string.chat_system_two_agents_typing, agents[0].name.cutName(), agents[1].name.cutName())
        else -> context.getString(
            R.string.chat_system_few_agents_typing,
            agents[0].name.cutName(),
            agents[1].name.cutName(),
            agents.size - 2
        )
    }
}

@BindingAdapter("inflateToolbar")
fun inflateToolbar(view: MaterialToolbar, agents: List<Agent>) {
    val context = view.context

    val typedArray = context.theme.obtainStyledAttributes(R.style.Widget_JivoSDK_Toolbar, R.styleable.JivoSDKToolbar)
    val logo = typedArray.getDrawable(R.styleable.JivoSDKToolbar_logo) ?: ContextCompat.getDrawable(context, R.drawable.jivo_sdk_vic_logo)
    val hasLogo = typedArray.getBoolean(R.styleable.JivoSDKToolbar_hideLogo, false)
    val title = typedArray.getString(R.styleable.JivoSDKToolbar_title) ?: context.getString(R.string.chat_title_placeholder)
    val subtitle = typedArray.getString(R.styleable.JivoSDKToolbar_subtitle) ?: context.getString(R.string.chat_subtitle_placeholder)
    typedArray.recycle()

    val agentsInChat = agents.filter { it.hasOnlineInChat && it.status !is AgentStatus.Offline }

    view.title = when {
        agentsInChat.isEmpty() -> title
        agentsInChat.size == 1 -> agentsInChat[0].name
        else -> {
            SpannableStringBuilder().apply {
                agentsInChat.forEachIndexed { index, agent ->
                    append(agent.name.split(" ").first())
                    if (index < agentsInChat.size - 1) {
                        append(", ")
                    }
                }
            }.toString()
        }
    }

    view.subtitle = subtitle

    if (!hasLogo) {
        val imageLoader = context.imageLoader
        val request = ImageRequest.Builder(view.context)
        when {
            agentsInChat.isEmpty() -> {
                request
                    .data(logo)
                    .size(40.dp)
                    .transformations(CircleCropTransformation())
                    .target {
                        view.logo = it
                    }
            }
            agentsInChat.size == 1 -> {
                request
                    .data(agentsInChat[0].photo)
                    .placeholder(R.drawable.jivo_sdk_vic_avatar_empty)
                    .error(if (agentsInChat[0].isBot()) R.drawable.jivo_sdk_ic_avatar_bot else R.drawable.jivo_sdk_vic_avatar_empty)
                    .size(40.dp)
                    .transformations(CircleCropTransformation())
                    .target {
                        view.logo = it
                    }
            }
            agentsInChat.size > 1 -> {
                view.logo = null
            }
        }
        imageLoader.enqueue(request.build())
    } else {
        view.logo = null
    }
}

@BindingAdapter("agentImageLoader")
fun agentImageLoader(layout: ViewGroup, state: MediaItemState?) {
    if (state == null) return

    val context = layout.context

    var viewHolder: ImageViewHolder? = layout.tag as ImageViewHolder?
    if (viewHolder == null) {
        viewHolder = ImageViewHolder(layout)
    }

    viewHolder.imageView.setImageDrawable(null)

    when (state) {
        MediaItemState.Initial -> {
            viewHolder.placeholder?.isVisible = true
        }
        MediaItemState.Loading -> {
            viewHolder.progressView?.isVisible = true
        }
        is MediaItemState.Success -> {
            val imageLoader = viewHolder.imageView.context.imageLoader
            val request = ImageRequest.Builder(viewHolder.imageView.context)
                .data(state.media.thumb(220.dp))
                .target(
                    onStart = {
                        viewHolder.progressView?.isVisible = true
                        viewHolder.placeholder?.isVisible = true
                        viewHolder.errorText?.isVisible = false
                    },
                    onSuccess = { result ->
                        viewHolder.placeholder?.isVisible = false
                        viewHolder.progressView?.isVisible = false
                        viewHolder.imageView.setImageDrawable(result)
                    },
                    onError = {
                        viewHolder.progressView?.isVisible = false
                        viewHolder.errorText?.text = context.getString(R.string.media_uploading_common_error)
                        viewHolder.errorText?.isVisible = true
                    }
                )
                .build()
            imageLoader.enqueue(request)
        }
        MediaItemState.Expired -> {
            viewHolder.placeholder?.isVisible = true
            viewHolder.progressView?.isVisible = false
            viewHolder.errorText?.isVisible = true
        }
        is MediaItemState.Error -> {
            viewHolder.progressView?.isVisible = false
            viewHolder.errorText?.text = context.getString(R.string.media_uploading_common_error)
            viewHolder.errorText?.isVisible = true
        }
    }
}

@BindingAdapter("clientImageLoader")
fun clientImageLoader(layout: ViewGroup, state: FileState?) {
    val uri = state?.uri ?: return

    val context = layout.context

    var viewHolder: ImageViewHolder? = layout.tag as ImageViewHolder?
    if (viewHolder == null) {
        viewHolder = ImageViewHolder(layout)
    }

    viewHolder.imageView.setImageDrawable(null)

    val imageLoader = viewHolder.imageView.context.imageLoader
    val requestBuilder = ImageRequest.Builder(viewHolder.imageView.context)
        .data(uri)

    if (URLUtil.isContentUrl(uri)) {
        viewHolder.progressView?.isVisible = state.uploadState is UploadState.Uploading
        viewHolder.status?.setImageResource(
            when (state.uploadState) {
                is UploadState.Uploading -> R.drawable.jivo_sdk_vic_message_status_sending
                is UploadState.Error -> R.drawable.jivo_sdk_vic_message_status_error
            }
        )
        requestBuilder.target(
            onStart = {
                viewHolder.placeholder?.isVisible = true
                viewHolder.errorText?.isVisible = false
            },
            onSuccess = { result ->
                viewHolder.placeholder?.isVisible = false
                viewHolder.imageView.setImageDrawable(result)
            },
            onError = {
                viewHolder.progressView?.isVisible = true
                viewHolder.placeholder?.isVisible = true
                viewHolder.errorText?.text = context.getString(R.string.media_uploading_common_error)
                viewHolder.errorText?.isVisible = true

            }
        )
    } else {
        requestBuilder.target(
            onStart = {
                viewHolder.progressView?.isVisible = true
                viewHolder.placeholder?.isVisible = true
                viewHolder.errorText?.isVisible = false
            },
            onSuccess = { result ->
                viewHolder.progressView?.isVisible = false
                viewHolder.placeholder?.isVisible = false
                viewHolder.imageView.setImageDrawable(result)

            },
            onError = {
                viewHolder.progressView?.isVisible = true
                viewHolder.placeholder?.isVisible = true
                viewHolder.errorText?.text = context.getString(R.string.media_uploading_common_error)
                viewHolder.errorText?.isVisible = true

            }
        )
    }
    imageLoader.enqueue(requestBuilder.build())
}

private class ImageViewHolder(layout: ViewGroup) {
    val imageView: AppCompatImageView = layout.findViewById(R.id.image)
    val progressView: ProgressBar? = layout.findViewById(R.id.progress)
    val status: AppCompatImageView? = layout.findViewById(R.id.status)
    val time: TextView? = layout.findViewById(R.id.time)
    val errorText: TextView? = layout.findViewById(R.id.error)
    val placeholder: AppCompatImageView? = layout.findViewById(R.id.placeholder)
}

@BindingAdapter("fileIcon")
fun setFileIcon(view: AppCompatImageView, type: String?) {

    view.load(
        when (type?.getFileType()) {
            TYPE_DOCUMENT -> R.drawable.jivo_sdk_vic_file
            TYPE_IMAGE -> R.drawable.jivo_sdk_vic_image
            TYPE_VIDEO -> R.drawable.jivo_sdk_vic_video
            TYPE_AUDIO -> R.drawable.jivo_sdk_vic_audio
            else -> R.drawable.jivo_sdk_vic_file
        }
    )
}

@BindingAdapter("time")
fun setTime(view: TextView, time: Long?) {
    if (time == null || time == 0L) {
        return
    } else {
        view.text = DateFormat.getTimeFormat(view.context).format(Date(time * 1000))
    }
}

@BindingAdapter("agentName")
fun setAgentName(view: AppCompatTextView, name: String?) {
    if (name.isNullOrBlank()) {
        view.setText(R.string.agent_name_default)
    } else {
        view.text = name
    }
}

@BindingAdapter("mediaStatus")
fun setMediaStatus(view: TextView, state: MediaItemState?) {
    if (state == null) return
    val context = view.context

    when (state) {
        MediaItemState.Initial -> {}
        MediaItemState.Loading -> {
            view.isVisible = true
            view.isClickable = false
            view.text = context.getString(R.string.file_link_checking)
        }
        is MediaItemState.Success -> {
            if (!state.media.isExpired) {
                view.isVisible = true
                view.isClickable = true
                view.text = context.getString(R.string.message_download)
                view.paintFlags = view.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            } else {
                view.isInvisible = false
            }
        }
        MediaItemState.Expired -> view.isVisible = false
        is MediaItemState.Error -> {
            view.isVisible = true
            view.isClickable = false
            view.text = context.getString(R.string.download_status_error)
        }
    }
}

@BindingAdapter("fileName")
fun setFileName(view: TextView, state: MediaItemState?) {
    if (state == null) return
    when (state) {
        is MediaItemState.Success -> view.text = state.media.name.ifBlank { view.context.getString(R.string.file_name_unknown) }
        MediaItemState.Expired -> view.text = view.context.getString(R.string.file_download_expired)
        is MediaItemState.Error -> view.text = view.context.getString(R.string.file_download_unavailable)
        else -> view.text = ""
    }
}

@BindingAdapter("stateInputText")
fun setStateInputText(view: AppCompatEditText, isEnabled: Boolean) {
    view.hint =
        view.context.getString(if (isEnabled) R.string.input_message_placeholder else R.string.chat_input_status_contact_info)
    view.isEnabled = isEnabled
}

@BindingAdapter("isEndIconVisible")
fun setEndIconVisible(view: TextInputLayout, isVisible: Boolean) {
    view.isEndIconVisible = isVisible
}


