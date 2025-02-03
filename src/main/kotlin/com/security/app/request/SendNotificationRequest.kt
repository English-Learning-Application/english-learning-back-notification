package com.security.app.request

data class SendNotificationRequest(
    val notificationType: String,
    val channels: List<String>,
    val receiverId: String,
    val message: SendNotificationMessage
)

data class SendNotificationMessage(
    val otpCode: String?,
    val title: String?,
    val message: String?,
    val sessionId: String?,
)