package com.security.app.request

data class UpdateUserCredentialRequest(
    val userId: String,
    val username: String? = null,
    val userEmailAddress: String? = null,
    val userPhoneNumber: String? = null,
    val userFcmToken: String? = null
)