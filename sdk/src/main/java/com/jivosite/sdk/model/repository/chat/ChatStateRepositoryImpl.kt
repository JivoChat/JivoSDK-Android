package com.jivosite.sdk.model.repository.chat

import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 1/27/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ChatStateRepositoryImpl @Inject constructor(
    schedulers: Schedulers
) : StateRepository<ChatState>(schedulers, "ChatState", ChatState()), ChatStateRepository {

    override val state: ChatState
        get() = _state

    override val observableState: StateLiveData<ChatState>
        get() = _stateLive

    override fun setVisibility(isVisible: Boolean) = updateStateInDispatchingThread {
        transform { state -> state.copy(visible = isVisible) }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform { ChatState() }
    }
}