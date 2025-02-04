package com.security.app.controllers

import com.security.app.entities.Notification
import com.security.app.entities.UserNotificationCredential
import com.security.app.model.ListMessage
import com.security.app.model.Message
import com.security.app.request.DeleteFcmTokenRequest
import com.security.app.request.NotificationSendCallback
import com.security.app.request.SendNotificationRequest
import com.security.app.request.UpdateUserCredentialRequest
import com.security.app.services.MessageService
import com.security.app.services.UserNotificationCredentialService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val messageService: MessageService,
    private val notificationCredentialService: UserNotificationCredentialService
) {
    @PostMapping("/send")
    fun sendMessage(
        @RequestBody notificationRequest: SendNotificationRequest
    ): ListMessage<Notification> {
        try {
            val notifications = messageService.sendMessage(notificationRequest)
            if (notifications.isNullOrEmpty()) {
                return ListMessage.BadRequest(
                    "Failed to send notification"
                )
            }
            return ListMessage.Success("Notification sent successfully", notifications)
        } catch (e: Exception) {
            return ListMessage.BadRequest(e.message ?: "Failed to send notification")
        }
    }

    @PostMapping("/credentials")
    fun updateCredentials(
        @RequestBody updateNotificationCredentialRequest: UpdateUserCredentialRequest
    ): Message<UserNotificationCredential> {
        try {
            val userNotificationCredential =
                messageService.updateUserNotificationCredential(updateNotificationCredentialRequest)
            return Message.Success("User notification credential updated successfully", userNotificationCredential)
        } catch (e: Exception) {
            return Message.BadRequest(e.message ?: "Failed to update user notification credential")
        }
    }

    @PostMapping("/credentials/delete-fcm")
    fun deleteFcmToken(
        @RequestBody deleteFcmTokenRequest: DeleteFcmTokenRequest
    ): Message<UserNotificationCredential> {
        try {
            val userNotificationCredential = notificationCredentialService.deleteFcmTokens(
                deleteFcmTokenRequest.userId,
                deleteFcmTokenRequest.fcmTokens
            )
            return Message.Success("Fcm token deleted successfully", userNotificationCredential)
        } catch (e: Exception) {
            return Message.BadRequest(e.message ?: "Failed to delete fcm token")
        }
    }

    @PostMapping("/handle/{notificationId}")
    fun handleNotification(
        @PathVariable notificationId: String,
        @RequestBody callbackRequest: NotificationSendCallback
    ): ResponseEntity<Message<Notification>> {
        try {
            val notification = messageService.handleNotification(notificationId, callbackRequest)
            return ResponseEntity.ok(Message.Success("Notification handled successfully", notification))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Message.BadRequest(e.message ?: "Failed to handle notification"))
        }
    }
}