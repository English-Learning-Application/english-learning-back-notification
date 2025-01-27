package com.security.app.controllers

import com.security.app.services.MessageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val messageService: MessageService
) {
    @PostMapping("/send")
    fun sendMessage(@RequestParam message: String): String {
        messageService.sendInstantMessage(message)
        return "Message sent successfully"
    }

    // Endpoint to send a scheduled message at a specific time
    @PostMapping("/send-scheduled")
    fun sendScheduledMessage(@RequestParam message: String, @RequestParam targetTime: String): String {
        val targetDateTime = LocalDateTime.parse(targetTime)
        messageService.sendScheduledMessageAtSpecificTime(message, targetDateTime)
        return "Scheduled message will be sent at $targetTime"
    }
}