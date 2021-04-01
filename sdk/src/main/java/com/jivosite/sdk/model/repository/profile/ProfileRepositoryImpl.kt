package com.jivosite.sdk.model.repository.profile

import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 12/13/20.
 *
 * Реализация репозитория для хранения информации о пользователе.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class ProfileRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val storage: SharedStorage
) : StateRepository<ProfileState>(schedulers, "Profile", ProfileState(storage.clientId)), ProfileRepository {

    override val state: ProfileState
        get() = _state

    override val observableState: StateLiveData<ProfileState>
        get() = _stateLive

    override val id: String
        get() = state.id

    override fun setId(id: String) = updateStateInDispatchingThread {
        doBefore { state -> state.id != id }
        transform { state -> state.copy(id = id) }
        doAfter { state -> storage.clientId = state.id }
    }

    override fun isMe(id: String): Boolean = state.id == id

    override fun clear() = updateStateInDispatchingThread {
        transform { ProfileState("") }
        doAfter { state -> storage.clientId = state.id }
    }
}