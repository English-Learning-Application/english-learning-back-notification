package com.security.app.utils

import com.security.app.entities.Notification
import com.security.app.model.SmsNotificationModel
import com.security.app.request.SendNotificationMessage
import org.springframework.stereotype.Component

@Component
class SmsTypeHandler {
    fun handleSmsType(
        smsType: String,
        notification: Notification,
        sendNotificationMessage: SendNotificationMessage
    ): SmsNotificationModel {
        return when (smsType) {
            "otp_confirm_sms" -> {
                val notificationTemplate = notification.smsNotificationTemplate
                val userCredential = notification.userNotificationCredential
                val message = notificationTemplate?.content?.replace(
                    "{otp}",
                    sendNotificationMessage.otpCode ?: ""
                ) ?: "Your OTP is ${sendNotificationMessage.otpCode}"
                val toPhoneNumber = userCredential.userPhoneNumber
                SmsNotificationModel(
                    notificationId = notification.id.toString(),
                    message = message,
                    toNumber = toPhoneNumber
                )
            }

            else -> throw IllegalArgumentException("Invalid sms type")
        }
    }
}