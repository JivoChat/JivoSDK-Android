package com.jivosite.sdk.model.repository.profile

import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 12/13/20.
 *
 * Репозиторий для хренения информации о профиле пользователя.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface ProfileRepository {

    val state: ProfileState

    val observableState: StateLiveData<ProfileState>

    val id: String

    /**
     * @param id Идентификатор пользователя.
     */
    fun setId(id: String)

    /**
     * @return true - если переданный идентификатор равен идентификатору пользователя.
     */
    fun isMe(id: String): Boolean
}