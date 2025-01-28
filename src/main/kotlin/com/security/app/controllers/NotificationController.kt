package com.security.app.controllers

import com.security.app.entities.Notification
import com.security.app.model.ListMessage
import com.security.app.request.SendNotificationRequest
import com.security.app.services.MessageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val messageService: MessageService
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
}