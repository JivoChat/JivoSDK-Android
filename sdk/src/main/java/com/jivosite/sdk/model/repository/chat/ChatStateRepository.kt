package com.jivosite.sdk.model.repository.chat

import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 1/27/21.
 *
 * Репозиторий для хранения информации по окну чата. Пока храним только состояние просмотра, то есть наличие окна с
 * чатом на переднем плане.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface ChatStateRepository {

    val state: ChatState

    val observableState: StateLiveData<ChatState>

    fun setVisibility(isVisible: Boolean)

    fun setBlacklisted()

    fun setSanctioned()

    fun clear()
}