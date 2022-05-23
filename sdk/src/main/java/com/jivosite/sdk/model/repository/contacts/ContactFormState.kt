package com.jivosite.sdk.model.repository.contacts

import com.squareup.moshi.JsonClass


/**
 * Created on 11.05.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
data class ContactFormState(
    val hasSentContactForm: Boolean = false,
    val contactForm: ContactForm? = null
) {
    val size: Int
        get() = if (contactForm != null) 1 else 0
}

@JsonClass(generateAdapter = true)
data class ContactForm(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val timestamp: Long = System.currentTimeMillis() / 1000,
)
