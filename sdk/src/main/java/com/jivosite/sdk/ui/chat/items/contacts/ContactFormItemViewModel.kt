package com.jivosite.sdk.ui.chat.items.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.contacts.ContactForm
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.ext.isEmailValid
import com.jivosite.sdk.support.ext.isPhoneValid
import com.jivosite.sdk.support.ext.requireValue
import com.jivosite.sdk.ui.chat.items.ContactFormEntry
import com.jivosite.sdk.ui.chat.items.message.general.ChatEntryViewModel
import javax.inject.Inject

/**
 * Created on 14.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class ContactFormItemViewModel @Inject constructor(
    val storage: SharedStorage,
    private val contactFormRepository: ContactFormRepository,
    private val agentRepository: AgentRepository
) : ChatEntryViewModel<ContactFormEntry>() {


    val name = MediatorLiveData<String>().apply {
        value = ""
        addSource(_entry) { value = it.state.contactForm?.name ?: "" }
    }
    val phone = MediatorLiveData<String>().apply {
        value = ""
        addSource(_entry) { value = it.state.contactForm?.phone ?: "" }
    }
    val email = MediatorLiveData<String>().apply {
        value = ""
        addSource(_entry) { value = it.state.contactForm?.email ?: "" }
    }

    private val _canSendState = MediatorLiveData<CanSendState>().apply {
        value = CanSendState()

        addSource(_entry) {
            value = value?.copy(hasSentContactForm = it.state.hasSentContactInfo)
        }

        addSource(name) {
            value = value?.copy(isNameValid = it.trim().isNotBlank())
        }
        addSource(phone) {
            value = value?.copy(isPhoneValid = it.trim().isPhoneValid())
        }
        addSource(email) {
            value = value?.copy(isEmailValid = it.trim().isEmailValid())
        }
    }

    val isNameValid: LiveData<Boolean> = _canSendState.map {
        it.isNameValid
    }

    val isPhoneValid: LiveData<Boolean> = _canSendState.map {
        it.isPhoneValid
    }

    val isEmailValid: LiveData<Boolean> = _canSendState.map {
        it.isEmailValid
    }

    val hasSentContacts = _entry.map {
        it.state.hasSentContactInfo
    }

    val canSend = _canSendState.map {
        it != null && !it.hasSentContactForm && it.isNameValid && it.isPhoneValid && it.isEmailValid
    }

    val hasAgentsOnline = agentRepository.hasAgentsOnline.map {
        it
    }

    fun sendContactForm() {
        contactFormRepository.setContactForm(
            ContactForm(
                name = name.requireValue(),
                phone = phone.requireValue(),
                email = email.requireValue()
            )
        )
    }

    private data class CanSendState(
        val hasSentContactForm: Boolean = false,
        val isNameValid: Boolean = false,
        val isPhoneValid: Boolean = false,
        val isEmailValid: Boolean = false
    )
}