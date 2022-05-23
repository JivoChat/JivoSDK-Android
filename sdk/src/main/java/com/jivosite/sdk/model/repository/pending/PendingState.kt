package com.jivosite.sdk.model.repository.pending

import com.jivosite.sdk.model.pojo.message.ClientMessage

/**
 * Created on 27.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
data class PendingState(
    val message: ClientMessage? = null
) {
    val size: Int
        get() = if (message != null) 1 else 0
}