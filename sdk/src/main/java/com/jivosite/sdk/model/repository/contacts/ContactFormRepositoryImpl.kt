package com.jivosite.sdk.model.repository.contacts

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.CustomData
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository.Companion.CREATE_CONTACT_FORM_TIMEOUT
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.builders.ContactInfo
import com.jivosite.sdk.support.builders.ContactInfo.Companion.contactInfo
import com.jivosite.sdk.support.ext.fromJson
import com.jivosite.sdk.support.ext.toJson
import com.jivosite.sdk.support.vm.StateLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

/**
 * Created on 11.05.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class ContactFormRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val storage: SharedStorage,
    private val moshi: Moshi,
    private val messageTransmitter: Transmitter
) : StateRepository<ContactFormState>(
    schedulers, "ContactForm", ContactFormState(hasSentContactInfo = storage.hasSentContactInfo)
),
    ContactFormRepository {

    override val observableState: StateLiveData<ContactFormState>
        get() = _stateLive

    override fun createContactForm(hasTimeout: Boolean) =
        updateStateInRepositoryThread(CREATE_CONTACT_FORM_TIMEOUT.takeIf { hasTimeout } ?: 0L
        ) {
            doBefore { state -> state.contactForm == null && !storage.hasSentContactInfo }
            transform { state -> state.copy(contactForm = ContactForm()) }
        }


    override fun setContactForm(contactForm: ContactForm) = updateStateInRepositoryThread {
        transform { state ->
            state.copy(
                hasSentContactInfo = true,
                contactForm = state.contactForm?.copy(
                    name = contactForm.name,
                    phone = contactForm.phone,
                    email = contactForm.email
                )
            )
        }
        doAfter { state ->
            sendContactInfo(
                contactInfo {
                    name = state.contactForm?.name
                    phone = state.contactForm?.phone ?: ""
                    email = state.contactForm?.email ?: ""
                }
            )
        }
    }

    override fun prepareToSendContactInfo(contactInfo: ContactInfo?) {
        if (contactInfo != null) {
            val jsonContactInfo = moshi.toJson(contactInfo)

            if (jsonContactInfo != storage.contactInfo) {
                storage.hasSentContactInfo = false
                storage.contactInfo = jsonContactInfo
                sendContactInfo(contactInfo)
            }

        } else if (!storage.hasSentContactInfo && storage.contactInfo.isNotBlank()) {
            moshi.fromJson<ContactInfo>(storage.contactInfo)?.let {
                sendContactInfo(it)
            }
        }
    }

    override fun sendCustomData(customDataFields: List<CustomData>?) {
        if (customDataFields != null) {
            val jsonCustomData =
                moshi.adapter<List<CustomData>>(Types.newParameterizedType(List::class.java, CustomData::class.java))
                    .toJson(customDataFields)

            if (jsonCustomData != storage.customData) {
                storage.hasSentCustomData = false
                storage.customData = jsonCustomData
            }

            if (!storage.hasSentCustomData) {
                messageTransmitter.sendMessage(SocketMessage.customData(jsonCustomData))
                storage.hasSentCustomData = true
            }

        } else if (!storage.hasSentCustomData && storage.customData.isNotBlank()) {
            messageTransmitter.sendMessage(SocketMessage.customData(storage.customData))
            storage.hasSentCustomData = true
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform {
            ContactFormState()
        }
        doAfter {
            storage.apply {
                hasSentContactInfo = false
                contactInfo = ""
                hasSentCustomData = false
                customData = ""
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