package com.security.app.request

data class NotificationSendCallback(
    val status: String,
    val channel: String,
    val fcmToken: String?
)