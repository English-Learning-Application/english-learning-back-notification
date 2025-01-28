package com.security.app.services

import com.security.app.entities.Notification
import com.security.app.repositories.NotificationRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {
    @Transactional
    fun saveNotification(notification: Notification): Notification {
        return notificationRepository.save(notification)
    }
}