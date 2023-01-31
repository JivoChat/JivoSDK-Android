package com.jivosite.sdk.model.repository.chat

import android.text.format.DateUtils
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.utils.after
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 1/27/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ChatStateRepositoryImpl @Inject constructor(
    private val storage: SharedStorage,
    schedulers: Schedulers
) : StateRepository<ChatState>(
    schedulers, "ChatState",
    ChatState(blacklisted = storage.blacklistedTime.after(), sanctioned = storage.blacklistedTime.after())
),
    ChatStateRepository {

    override val state: ChatState
        get() = _state

    override val observableState: StateLiveData<ChatState>
        get() = _stateLive

    override fun setVisibility(isVisible: Boolean) = updateStateInDispatchingThread {
        transform { state -> state.copy(visible = isVisible) }
    }

    override fun setBlacklisted() = updateStateInRepositoryThread {
        transform { state -> state.copy(blacklisted = true) }
        doAfter { storage.blacklistedTime = System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS }
    }

    override fun setSanctioned() = updateStateInRepositoryThread {
        transform { state -> state.copy(sanctioned = true) }
        doAfter { storage.sanctionedTime = System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform { ChatState() }
    }
}