package com.jivosite.sdk.support.buildersimport com.squareup.moshi.JsonClass/** * Created on 26.05.2021. * * @author Alexander Tavtorkin (tavtorkin@jivosite.com) * * Контактные данные клиента. Данные отображаются оператору, как будто их ввел клиент в форме контактов. * * @property name Имя клиента. * @property email Email клиента. * @property phone Номер телефона клиента. * @property description Дополнительная информация по клиенту (отобразится в поле "Описание" - раздел "О клиенте") * */@JsonClass(generateAdapter = true)class ContactInfo(    val name: String?,    val email: String,    val phone: String,    val description: String?) {    private constructor(builder: Builder) : this(builder.name, builder.email, builder.phone, builder.description)    companion object {        inline fun contactInfo(block: Builder.() -> Unit) = Builder().apply(block).build()    }    class Builder {        var name: String? = null        var email: String = ""        var phone: String = ""        var description: String? = null        fun build() = ContactInfo(this)    }}