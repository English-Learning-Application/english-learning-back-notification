package com.security.app.repositories

import com.security.app.entities.SmsNotificationTemplate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SmsNotificationTemplateRepository : JpaRepository<SmsNotificationTemplate, UUID> {
    fun findByTemplateType(templateType: String): SmsNotificationTemplate?
}