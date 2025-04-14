package com.jivosite.sdk.support.usecase

import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.storage.SharedStorage
import javax.inject.Inject

/**
 * Created on 10.04.2025.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class RecoveryClientIdUseCase @Inject constructor(
    private val storage: SharedStorage,
    private val profileRepository: ProfileRepository,
) : UseCase {

    override fun execute() {
        val clientId = storage.clientId
        val path = storage.path
        if (clientId.isBlank() && path.isNotBlank()) {
            val list = path.split(":")
            list.getOrNull(2)?.let {
                storage.clientId = it
                profileRepository.setId(it)
            }
        }
    }
}