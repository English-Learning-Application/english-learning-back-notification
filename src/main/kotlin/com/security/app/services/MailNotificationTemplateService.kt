package com.security.app.services

import com.security.app.entities.MailNotificationTemplate
import com.security.app.repositories.MailNotificationTemplateRepository
import org.springframework.stereotype.Service

@Service
class MailNotificationTemplateService(
    private val mailNotificationTemplateRepository: MailNotificationTemplateRepository
) {
    fun getTemplateByType(templateType: String): MailNotificationTemplate? {
        return mailNotificationTemplateRepository.findByTemplateType(templateType)
    }
}