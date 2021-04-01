package com.jivosite.sdk.socket.transmitter

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import javax.inject.Inject

/**
 * Created on 12/15/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class DefaultTransmitter @Inject constructor() : Transmitter {

    private val subscribers = mutableListOf<TransmitterSubscriber>()

    override fun addSubscriber(subscriber: TransmitterSubscriber) {
        subscribers.add(subscriber)
    }

    override fun removeSubscriber(subscriber: TransmitterSubscriber) {
        subscribers.remove(subscriber)
    }

    override fun sendMessage(message: SocketMessage) {
        subscribers.forEach { it.sendMessage(message) }
    }

    override fun sendMessage(message: String) {
        subscribers.forEach { it.sendMessage(message) }
    }

    override fun clear() {
        subscribers.clear()
    }
}