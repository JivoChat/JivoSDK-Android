package com.jivosite.sdk.model.repository.mediaimport androidx.lifecycle.LiveDataimport com.jivosite.sdk.network.resource.Resource/** * Created on 12.10.2021. * * @author Alexander Tavtorkin (av.tavtorkin@gmail.com) */interface MediaRepository {    fun createRequest(path: String): LiveData<Resource<Unit>>}