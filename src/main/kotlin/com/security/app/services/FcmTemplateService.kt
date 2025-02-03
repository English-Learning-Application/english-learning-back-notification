package com.security.app.services

import com.security.app.entities.PushNotificationTemplate
import com.security.app.repositories.PushNotificationTemplateRepository
import org.springframework.stereotype.Service

@Service
class FcmTemplateService(
    private val pushNotificationTemplateRepository: PushNotificationTemplateRepository
) {
    fun getTemplateByType(templateType: String): PushNotificationTemplate? {
        return pushNotificationTemplateRepository.findByTemplateType(templateType)
    }
}