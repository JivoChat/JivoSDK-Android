package com.jivosite.sdk.model.repository.contacts

import com.jivosite.sdk.support.builders.ContactInfo
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 11.05.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
interface ContactFormRepository {

    companion object {
        const val CREATE_CONTACT_FORM_TIMEOUT = 1000L
    }

    val observableState: StateLiveData<ContactFormState>

    fun createContactForm(hasTimeout: Boolean = false)

    fun sendContactForm(contactForm: ContactForm)

    fun prepareToSendContactInfo(contactInfo: ContactInfo? = null)

    fun clear()
}