package com.jivosite.sdk.model.repository.contacts

import com.jivosite.sdk.model.pojo.CustomData
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

    fun setContactForm(contactForm: ContactForm)

    fun createContactForm(hasTimeout: Boolean = false)

    fun prepareToSendContactInfo(contactInfo: ContactInfo)

    fun prepareToSendCustomData(customDataFields: List<CustomData>)

    fun clear()
}