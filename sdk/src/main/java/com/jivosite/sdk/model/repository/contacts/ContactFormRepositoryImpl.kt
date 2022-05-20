package com.jivosite.sdk.model.repository.contacts

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository.Companion.CREATE_CONTACT_FORM_TIMEOUT
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.builders.ClientInfo
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 11.05.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class ContactFormRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val storage: SharedStorage
) : StateRepository<ContactFormState>(schedulers, "ContactForm", ContactFormState(hasSentContactForm = storage.hasSentContactForm)),
    ContactFormRepository {

    override val observableState: StateLiveData<ContactFormState>
        get() = _stateLive

    override fun createContactForm() = updateStateInRepositoryThread(CREATE_CONTACT_FORM_TIMEOUT) {
        doBefore { state -> state.contactForm == null }
        transform { state -> state.copy(contactForm = ContactForm()) }
    }

    override fun sendContactForm(contactForm: ContactForm) = updateStateInRepositoryThread {
        transform { state ->
            Jivo.setClientInfo(
                ClientInfo.Builder()
                    .setName(contactForm.name)
                    .setEmail(contactForm.email)
                    .setPhone(contactForm.phone)
                    .build()
            )
            state.copy(contactForm = contactForm)
        }
    }

    override fun sentContactForm() = updateStateInRepositoryThread {
        transform { state ->
            state.copy(hasSentContactForm = true)
        }
        doAfter {
            storage.hasSentContactForm = true
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform {
            storage.hasSentContactForm = false
            ContactFormState()
        }
    }
}