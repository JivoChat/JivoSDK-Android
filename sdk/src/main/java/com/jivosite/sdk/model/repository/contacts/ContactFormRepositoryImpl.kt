package com.jivosite.sdk.model.repository.contacts

import com.jivosite.sdk.model.pojo.CustomData
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository.Companion.CREATE_CONTACT_FORM_TIMEOUT
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.builders.ContactInfo
import com.jivosite.sdk.support.ext.toJson
import com.jivosite.sdk.support.usecase.SendContactInfoUseCase
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
    private val sendContactInfoUseCase: SendContactInfoUseCase,
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
        doBefore {
            storage.contactInfo = moshi.toJson(contactForm)
            true
        }
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
        doAfter {
            sendContactInfoUseCase.execute()
        }
    }

    override fun prepareToSendContactInfo(contactInfo: ContactInfo) {
        val jsonContactInfo = moshi.toJson(contactInfo)

        if (jsonContactInfo != storage.contactInfo) {
            storage.hasSentContactInfo = false
            storage.contactInfo = jsonContactInfo
        }
    }

    override fun prepareToSendCustomData(customDataFields: List<CustomData>) {
            val jsonCustomData =
                moshi.adapter<List<CustomData>>(Types.newParameterizedType(List::class.java, CustomData::class.java))
                    .toJson(customDataFields)

            if (jsonCustomData != storage.customData) {
                storage.hasSentCustomData = false
                storage.customData = jsonCustomData
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