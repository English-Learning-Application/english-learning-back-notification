package com.security.app.services

import com.security.app.entities.Notification
import com.security.app.repositories.NotificationRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {
    @Transactional
    fun saveNotification(notification: Notification): Notification {
        return notificationRepository.save(notification)
    }

    fun getNotificationById(id: UUID): Notification? {
        return notificationRepository.getNotificationById(id)
    }

    @Transactional
    fun saveAllNotifications(notifications: List<Notification>): List<Notification> {
        return notificationRepository.saveAll(notifications)
    }
}