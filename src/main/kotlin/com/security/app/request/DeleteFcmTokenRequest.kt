package com.security.app.request

data class DeleteFcmTokenRequest(
    val userId: String,
    val fcmTokens: List<String>
)