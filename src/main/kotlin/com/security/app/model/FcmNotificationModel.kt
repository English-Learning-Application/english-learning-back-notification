package com.security.app.model

data class FcmNotificationModel(
    val notificationId: String,
    val fcmToken: String,
    val title: String,
    val body: String,
    val action: String,
)
