package com.jivosite.sdk.support.usecase

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.support.builders.ContactInfo
import com.jivosite.sdk.support.ext.fromJson
import com.squareup.moshi.Moshi
import javax.inject.Inject

/**
 * Created on 19.04.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class SendContactInfoUseCase @Inject constructor(
    private val storage: SharedStorage,
    private val messageTransmitter: Transmitter,
    private val moshi: Moshi,
) : UseCase {

    override fun execute() {
        if (!storage.hasSentContactInfo && storage.contactInfo.isNotBlank()) {
            moshi.fromJson<ContactInfo>(storage.contactInfo)?.let {
                sendContactInfo(it)
            }
        }
    }

    private fun sendContactInfo(contactInfo: ContactInfo) {
        val map = mapOf(
            "atom/user.name" to contactInfo.name,
            "atom/user.email" to contactInfo.email,
            "atom/user.phone" to contactInfo.phone,
            "atom/user.desc" to contactInfo.description
        )

        for ((key, value) in map) {
            if (!value.isNullOrBlank()) {
                messageTransmitter.sendMessage(SocketMessage.contactInfo(key, value))
            }
        }
        if (contactInfo.email.isNotBlank() || contactInfo.phone.isNotBlank()) {
            storage.hasSentContactInfo = true
        }
        Jivo.i("Contact info sent successfully")
    }
}