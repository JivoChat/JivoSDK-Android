package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.unsupported.UnsupportedRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class FallbackDelegate @Inject constructor(
    private val unsupportedRepository: UnsupportedRepository
) : SocketMessageDelegate {

    override fun handle(message: SocketMessage) {
        unsupportedRepository.addMessage(message)
    }
}