package com.jivosite.sdk.ui.chat.items.message.file.agent

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.media.MediaRepository
import com.jivosite.sdk.support.ext.loadSilentlyResource
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentFileItemViewModel @Inject constructor(
    agentRepository: AgentRepository,
    private val mediaRepository: MediaRepository
) : MessageItemViewModel<AgentMessageEntry>(agentRepository) {

    private val _state = MediatorLiveData<FileItemState>().apply {
        value = FileItemState()
        addSource(_entry) {
            value = FileItemState(media = Media(path = it.data))
        }

    }
    val state: LiveData<FileItemState>
        get() = _state

    val fileName: LiveData<String> = Transformations.map(_entry) { entry ->
        File(Uri.parse(entry.message.data).path ?: "").name
    }

    val type: LiveData<String> = Transformations.map(_entry) {
        it.type
    }

    fun checkLink() {

        val path = _entry.value?.message?.data ?: ""

        if (path.startsWith("https://media")) {
            mediaRepository.createRequest(path).loadSilentlyResource {
                progress { updateState { it.copy(isLoading = true) } }
                result {
                    updateState {
                        it.copy(
                            hasCompletedCheck = true,
                            isLoading = false,
                            media = Media(isExpired = false, path = path)
                        )
                    }
                }
                error {
                    updateState {
                        it.copy(
                            hasCompletedCheck = true,
                            isLoading = false,
                            media = Media(isExpired = true, path = path)
                        )
                    }
                }
            }
        } else {
            updateState { it.copy(media = Media(path = path)) }
        }
    }

    private fun updateState(updater: (FileItemState) -> FileItemState) {
        _state.value = updater(_state.value ?: FileItemState())
    }
}

data class FileItemState(
    val hasCompletedCheck: Boolean = false,
    val isLoading: Boolean = false,
    val media: Media = Media()
)


data class Media(
    val path: String = "",
    val isExpired: Boolean = false
)
