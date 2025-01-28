package com.security.app.services

import com.security.app.entities.SmsNotificationTemplate
import com.security.app.repositories.SmsNotificationTemplateRepository
import org.springframework.stereotype.Service

@Service
class SmsNotificationTemplateService(
    private val smsNotificationTemplateRepository: SmsNotificationTemplateRepository
) {
    fun getTemplateByType(templateType: String): SmsNotificationTemplate? {
        return smsNotificationTemplateRepository.findByTemplateType(templateType)
    }
}