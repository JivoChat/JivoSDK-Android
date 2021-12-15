package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomMeUrlPathDelegate @Inject constructor(
    private val storage: SharedStorage
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/me.url.path"
    }

    override fun handle(message: SocketMessage) {
        message.data?.run {
            storage.path = this
        } ?: Jivo.e("""There is message "atom/me.url.path" without data property""")
    }
}