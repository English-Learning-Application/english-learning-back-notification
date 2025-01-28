package com.security.app.model

data class SmsNotificationModel(
    val toNumber: String,
    val message: String,
)