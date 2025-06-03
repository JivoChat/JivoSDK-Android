package com.jivosite.sdk.ui.chat.items.message.uploading.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.upload.FileState
import com.jivosite.sdk.ui.chat.items.UploadingFileEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import javax.inject.Inject

/**
 * Created on 2/24/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class UploadingImageItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<UploadingFileEntry>(agentRepository) {

    val state: LiveData<FileState> = _entry.map { entry ->
        entry.state
    }
}
