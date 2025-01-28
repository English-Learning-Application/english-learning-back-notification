package com.security.app.services

import com.security.app.entities.UserNotificationCredential
import com.security.app.repositories.UserNotificationCredentialsRepository
import com.security.app.utils.toUUID
import org.springframework.stereotype.Service

@Service
class UserNotificationCredentialService(
    private val userNotificationCredentialRepository: UserNotificationCredentialsRepository
) {
    fun getCredentialsByUserId(userId: String): UserNotificationCredential? {
        return userNotificationCredentialRepository.findByUserId(userId.toUUID())
    }
}