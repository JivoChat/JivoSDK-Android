package com.jivosite.sdk.socket.transmitter

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import java.util.*
import javax.inject.Inject

/**
 * Created on 12/15/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class DefaultTransmitter @Inject constructor() : Transmitter {

    private val subscribers = mutableListOf<TransmitterSubscriber>()

    private val queue = LinkedList<Any>()

    override fun addSubscriber(subscriber: TransmitterSubscriber) {
        subscribers.add(subscriber)
        if (queue.isNotEmpty()) {
            queue.forEach { message ->
                when (message) {
                    is String -> subscriber.sendMessage(message)
                    is SocketMessage -> subscriber.sendMessage(message)
                }
            }
            queue.clear()
        }
    }

    override fun removeSubscriber(subscriber: TransmitterSubscriber) {
        subscribers.remove(subscriber)
    }

    override fun sendMessage(message: SocketMessage) {
        if (subscribers.isEmpty()) {
            queue.add(message)
        } else {
            subscribers.forEach { it.sendMessage(message) }
        }
    }

    override fun sendMessage(message: String) {
        if (subscribers.isEmpty()) {
            queue.add(message)
        } else {
            subscribers.forEach { it.sendMessage(message) }
        }
    }

    override fun clear() {
        subscribers.clear()
    }
}