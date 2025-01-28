package com.security.app.model

data class MailNotificationModel(
    val subject: String,
    val body: String,
    val toEmail: String
)