package com.security.app.repositories

import com.security.app.entities.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotificationRepository : JpaRepository<Notification, UUID> {
    fun getNotificationById(id: UUID): Notification?
}