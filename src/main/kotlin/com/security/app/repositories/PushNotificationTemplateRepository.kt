package com.security.app.repositories

import com.security.app.entities.PushNotificationTemplate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PushNotificationTemplateRepository : JpaRepository<PushNotificationTemplate, UUID> {
    fun findByTemplateType(templateType: String): PushNotificationTemplate?
}