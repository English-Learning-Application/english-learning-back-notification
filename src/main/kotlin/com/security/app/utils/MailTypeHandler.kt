package com.security.app.utils

import com.security.app.entities.Notification
import com.security.app.model.MailNotificationModel
import com.security.app.request.SendNotificationMessage
import org.springframework.stereotype.Component

@Component
class MailTypeHandler {
    fun handleMailType(
        mailType: String,
        notification: Notification,
        sendNotificationMessage: SendNotificationMessage
    ): MailNotificationModel {
        return when (mailType) {
            "otp_confirm_email" -> {
                val notificationTemplate = notification.mailNotificationTemplate
                val userCredential = notification.userNotificationCredential
                val subject = notificationTemplate?.subject?.replace(
                    "{username}",
                    userCredential.username
                ) ?: "OTP Email Confirmation"
                val body = notificationTemplate?.content?.replace(
                    "{otp}",
                    sendNotificationMessage.otpCode ?: ""
                )?.replace("{username}", userCredential.username) ?: "Your OTP is ${sendNotificationMessage.otpCode}"
                val toEmail = userCredential.userEmailAddress
                MailNotificationModel(
                    notificationId = notification.id.toString(),
                    subject = subject,
                    body = body,
                    toEmail = toEmail
                )
            }

            "reset_password_email" -> {
                val notificationTemplate = notification.mailNotificationTemplate
                val userCredential = notification.userNotificationCredential
                val subject = notificationTemplate?.subject?.replace(
                    "{username}",
                    userCredential.username
                ) ?: "Reset Password"
                val body = notificationTemplate?.content?.replace(
                    "{otp}",
                    sendNotificationMessage.otpCode ?: ""
                )?.replace("{username}", userCredential.username) ?: "Your OTP is ${sendNotificationMessage.otpCode}"
                val toEmail = userCredential.userEmailAddress
                MailNotificationModel(
                    notificationId = notification.id.toString(),
                    subject = subject,
                    body = body,
                    toEmail = toEmail
                )
            }

            else -> throw IllegalArgumentException("Invalid mail type")
        }
    }
}