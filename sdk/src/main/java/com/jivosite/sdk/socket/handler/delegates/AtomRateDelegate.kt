package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.rating.RatingRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 06.03.2023.
 *
 * Example of message to handle:
 * {"type":"atom/chat.rate", "id":"1"}
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class AtomRateDelegate @Inject constructor(
    private val ratingRepository: RatingRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/chat.rate"
    }

    override fun handle(message: SocketMessage) {
        if (message.id == null) return
        ratingRepository.setChatId(message.id)
    }
}