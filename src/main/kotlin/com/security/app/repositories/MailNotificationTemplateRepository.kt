package com.security.app.repositories

import com.security.app.entities.MailNotificationTemplate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MailNotificationTemplateRepository : JpaRepository<MailNotificationTemplate, UUID> {
    fun findByTemplateType(templateType: String): MailNotificationTemplate?
}