package com.jivosite.sdk.model.repository.unsupported

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import java.util.*

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
data class UnsupportedState(val messages: List<SocketMessage> = Collections.emptyList())