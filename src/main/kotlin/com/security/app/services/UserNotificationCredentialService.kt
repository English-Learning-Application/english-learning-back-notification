package com.security.app.services

import com.security.app.entities.UserNotificationCredential
import com.security.app.repositories.UserNotificationCredentialsRepository
import com.security.app.request.UpdateUserCredentialRequest
import com.security.app.utils.JsonUtils
import com.security.app.utils.toUUID
import org.springframework.stereotype.Service

@Service
class UserNotificationCredentialService(
    private val userNotificationCredentialRepository: UserNotificationCredentialsRepository,
    private val jsonUtils: JsonUtils
) {
    fun getCredentialsByUserId(userId: String): UserNotificationCredential? {
        return userNotificationCredentialRepository.findByUserId(userId.toUUID())
    }

    fun updateUserCredential(request: UpdateUserCredentialRequest): UserNotificationCredential {
        val credential = userNotificationCredentialRepository.findByUserId(request.userId.toUUID())
        if (credential == null) {
            val fcmToken = if (request.userFcmToken != null) jsonUtils.toJson(request.userFcmToken) else null
            val userCredential = UserNotificationCredential().let {
                it.userEmailAddress = request.userEmailAddress ?: ""
                it.userPhoneNumber = request.userPhoneNumber ?: ""
                it.userFcmToken = fcmToken ?: ""
                it.username = request.username ?: ""
                it.userId = request.userId.toUUID()
                it
            }

            return userNotificationCredentialRepository.save(userCredential)
        } else {
            if (credential.userFcmToken.isEmpty()) {
                credential.userFcmToken =
                    if (request.userFcmToken != null) jsonUtils.toJson(
                        listOf(
                            request.userFcmToken
                        ),
                    ) else ""
            } else {
                /// Map to list then add the encoded again
                val fcmTokenList = jsonUtils.fromJson(credential.userFcmToken, List::class.java)
                val fcmStringList: MutableList<String> = fcmTokenList.mapNotNull { it as? String }.toMutableList()
                if (request.userFcmToken != null && !fcmStringList.contains(request.userFcmToken)) {
                    fcmStringList.add(request.userFcmToken)
                }
                credential.userFcmToken = jsonUtils.toJson(fcmStringList)
            }
            credential.userEmailAddress = request.userEmailAddress ?: ""
            credential.userPhoneNumber = request.userPhoneNumber ?: ""
            credential.username = request.username ?: ""
            return userNotificationCredentialRepository.save(credential)
        }
    }
}