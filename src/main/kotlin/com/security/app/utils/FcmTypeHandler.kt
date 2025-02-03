package com.security.app.utils

import com.security.app.entities.Notification
import com.security.app.model.FcmNotificationModel
import com.security.app.request.SendNotificationMessage
import org.springframework.stereotype.Component

@Component
class FcmTypeHandler(
    private val jsonUtils: JsonUtils
) {
    fun handleFcmType(
        fcmType: String,
        notification: Notification,
        sendNotificationMessage: SendNotificationMessage
    ): List<FcmNotificationModel> {
        return when (fcmType) {
            "fcm_chat_message" -> {
                val notificationTemplate = notification.pushNotificationTemplate
                val userCredential = notification.userNotificationCredential

                val title = notificationTemplate?.title?.replace(
                    "{username}",
                    userCredential.username
                ) ?: "New Message"

                val body = notificationTemplate?.body?.replace(
                    "{message}",
                    sendNotificationMessage.message ?: ""
                ) ?: "You have a new message"

                val action = notificationTemplate?.action?.replace(
                    "{sessionId}",
                    sendNotificationMessage.sessionId ?: ""
                ) ?: ""

                val fcmTokenList: List<String> =
                    jsonUtils.fromJson(userCredential.userFcmToken, List::class.java).mapNotNull { it as? String }

                fcmTokenList.map {
                    FcmNotificationModel(
                        notificationId = notification.id.toString(),
                        title = title,
                        body = body,
                        action = action,
                        fcmToken = it
                    )
                }
            }

            else -> {
                throw IllegalArgumentException("Invalid FCM type")
            }
        }
    }
}