package com.security.app.model

data class SmsNotificationModel(
    val notificationId: String,
    val toNumber: String,
    val message: String,
)