package com.jivosite.sdk.ui.chat.items.message.uploading.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.upload.FileState
import com.jivosite.sdk.ui.chat.items.UploadingFileEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import javax.inject.Inject

/**
 * Created on 2/24/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class UploadingImageItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<UploadingFileEntry>(agentRepository) {

    val state: LiveData<FileState> = Transformations.map(_entry) { entry ->
        entry.state
    }
}
