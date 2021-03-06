package com.jivosite.sdk.ui.chat.items.message.uploading.file

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.upload.FileState
import com.jivosite.sdk.model.repository.upload.UploadState
import com.jivosite.sdk.ui.chat.items.UploadingFileEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import javax.inject.Inject

/**
 * Created on 2/25/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class UploadingFileItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<UploadingFileEntry>(agentRepository) {

    val state: LiveData<FileState> = Transformations.map(_entry) { entry ->
        entry.state
    }

    val fileName: LiveData<String> = Transformations.map(state) {
        it.name
    }

    val size: LiveData<Long> = Transformations.map(state) {
        it.size
    }

    val type: LiveData<String> = Transformations.map(state) {
        it.mimeType
    }

    val isError: LiveData<Boolean> = Transformations.map(state) {
        it.uploadState is UploadState.Error
    }
}
