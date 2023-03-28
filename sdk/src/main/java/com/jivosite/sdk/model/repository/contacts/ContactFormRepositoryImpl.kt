package com.jivosite.sdk.model.repository.contacts

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.CustomData
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
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
    private val messageTransmitter: Transmitter,
    private val connectionStateRepository: ConnectionStateRepository
) : StateRepository<ContactFormState>(
    schedulers, "ContactForm", ContactFormState(hasSentContactInfo = storage.hasSentContactInfo)
),
    ContactFormRepository {

    private lateinit var clientId: String

    override val observableState: StateLiveData<ContactFormState>
        get() = _stateLive

    override fun createContactForm(hasTimeout: Boolean) =
        updateStateInRepositoryThread(CREATE_CONTACT_FORM_TIMEOUT.takeIf { hasTimeout } ?: 0L
        ) {
            doBefore { state -> state.contactForm == null }
            transform { state -> state.copy(contactForm = ContactForm()) }
        }

    override fun sendContactForm(contactForm: ContactForm) = updateStateInRepositoryThread {
        transform { state ->
            sendContactInfo(
                contactInfo {
                    name = contactForm.name
                    email = contactForm.email
                    phone = contactForm.phone
                }
            )
            state.copy(
                contactForm = contactForm.copy(
                    name = contactForm.name,
                    phone = contactForm.phone,
                    email = contactForm.email
                ),
                hasSentContactInfo = true
            )
        }
        doAfter {
            storage.contactInfo = moshi.toJson(contactForm)
            storage.hasSentContactInfo = true
        }
    }

    override fun prepareToSendContactInfo(contactInfo: ContactInfo?) {
        if (contactInfo != null) {
            val jsonContactInfo = moshi.toJson(contactInfo)

            if (jsonContactInfo != storage.contactInfo) {
                storage.hasSentContactInfo = false
            }

            if (checkTermsToSend()) {
                if (!storage.hasSentContactInfo && jsonContactInfo != storage.contactInfo) {
                    storage.contactInfo = jsonContactInfo
                    sendContactInfo(contactInfo)

                    if (contactInfo.email.isNotBlank() || contactInfo.phone.isNotBlank()) {
                        setHasSentContactInfo()
                        storage.hasSentContactInfo = true
                    }
                }
            } else if (jsonContactInfo != storage.contactInfo) {
                storage.contactInfo = jsonContactInfo
                storage.hasSentContactInfo = false
            }


        } else if (checkTermsToSend() && !storage.hasSentContactInfo && storage.contactInfo.isNotBlank()) {
            moshi.fromJson<ContactInfo>(storage.contactInfo)?.let {
                sendContactInfo(it)
                storage.hasSentContactInfo = true
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

            if (checkTermsToSend() && !storage.hasSentCustomData) {
                messageTransmitter.sendMessage(SocketMessage.customData(jsonCustomData))
                storage.customData = jsonCustomData
                storage.hasSentCustomData = true
            }

        } else if (checkTermsToSend() && !storage.hasSentCustomData && storage.customData.isNotBlank()) {
            messageTransmitter.sendMessage(SocketMessage.customData(storage.customData))
            storage.hasSentCustomData = true
        }
    }

    private fun checkTermsToSend(): Boolean {
        clientId = storage.clientId

        if (clientId.isBlank()) {
            Jivo.w("Can not send contact info without client id = $clientId")
            return false
        }

        if (connectionStateRepository.state.value != ConnectionState.Connected) {
            return false
        }
        return true
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
                messageTransmitter.sendMessage(SocketMessage.contactInfo(key, value, clientId))
            }
        }
        Jivo.i("Contact info sent successfully")
    }

    private fun setHasSentContactInfo() = updateStateInRepositoryThread {
        transform { state ->
            state.copy(hasSentContactInfo = true)
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
}