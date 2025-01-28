package com.security.app.services

import com.security.app.entities.Notification
import com.security.app.model.MailNotificationModel
import com.security.app.model.NotificationStatus
import com.security.app.model.SmsNotificationModel
import com.security.app.request.SendNotificationRequest
import com.security.app.utils.JsonUtils
import com.security.app.utils.MailTypeHandler
import com.security.app.utils.SmsTypeHandler
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.util.*

@Service
class MessageService(
    private val amazonSQS: SqsAsyncClient,
    private val jsonUtils: JsonUtils,
    private val mailNotificationTemplateService: MailNotificationTemplateService,
    private val userNotificationCredentialService: UserNotificationCredentialService,
    private val notificationService: NotificationService,
    private val mailTypeHandler: MailTypeHandler,
    private val smsNotificationTemplateService: SmsNotificationTemplateService,
    private val smsTypeHandler: SmsTypeHandler,
) {
    private final val queueUrl = System.getenv("SQS_URL")

    fun sendMessage(message: SendNotificationRequest): List<Notification>? {

        val requestList = mutableListOf<SendMessageRequest>()
        val notificationsList = mutableListOf<Notification>()

        /// This is for mail notification
        var mailNotificationModel: MailNotificationModel? = null
        if (message.channels.contains("mail")) {
            val mailNotification = handleMailNotification(message)
            if (mailNotification != null) {
                notificationsList.add(mailNotification)
                mailNotificationModel =
                    mailTypeHandler.handleMailType(mailNotification.templateType, mailNotification, message.message)

                val request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(jsonUtils.toJson(mailNotificationModel))
                    .messageAttributes(
                        mapOf(
                            "channel" to MessageAttributeValue.builder()
                                .dataType("String")
                                .stringValue("mail")
                                .build(),
                        )
                    )
                requestList.add(request.build())
            } else {
                return null
            }
        }

        /// This is for sms notification
        var smsNotificationModel: SmsNotificationModel? = null
        if (message.channels.contains("sms")) {
            val smsNotification = handleSmsNotification(message)
            if (smsNotification != null) {
                notificationsList.add(smsNotification)
                smsNotificationModel =
                    smsTypeHandler.handleSmsType(smsNotification.templateType, smsNotification, message.message)

                val request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(jsonUtils.toJson(smsNotificationModel))
                    .messageAttributes(
                        mapOf(
                            "channel" to MessageAttributeValue.builder()
                                .dataType("String")
                                .stringValue("sms")
                                .build(),
                        )
                    )
                requestList.add(request.build())
            } else {
                return null
            }
        }

        val sendMessageBatchRequest = SendMessageBatchRequest.builder()
            .queueUrl(queueUrl)
            .entries(
                requestList.map { request ->
                    SendMessageBatchRequestEntry.builder()
                        .id(UUID.randomUUID().toString())
                        .messageBody(request.messageBody())
                        .messageAttributes(request.messageAttributes())
                        .build()
                }
            )

        amazonSQS.sendMessageBatch(sendMessageBatchRequest.build()).get()

        return notificationsList
    }

    fun handleMailNotification(message: SendNotificationRequest): Notification? {
        val mailNotificationTemplate = mailNotificationTemplateService.getTemplateByType(message.notificationType)
            ?: return null

        val userCredential = userNotificationCredentialService.getCredentialsByUserId(message.receiverId)
            ?: return null

        val notification = Notification().let {
            it.mailNotificationTemplate = mailNotificationTemplate
            it.status = NotificationStatus.PENDING
            it.templateType = message.notificationType
            it.userNotificationCredential = userCredential
            it
        }

        val savedNotification = notificationService.saveNotification(notification)

        return savedNotification
    }

    fun handleSmsNotification(message: SendNotificationRequest): Notification? {
        val mailNotificationTemplate = smsNotificationTemplateService.getTemplateByType(message.notificationType)
            ?: return null

        val userCredential = userNotificationCredentialService.getCredentialsByUserId(message.receiverId)
            ?: return null

        val notification = Notification().let {
            it.smsNotificationTemplate = mailNotificationTemplate
            it.status = NotificationStatus.PENDING
            it.templateType = message.notificationType
            it.userNotificationCredential = userCredential
            it
        }

        val savedNotification = notificationService.saveNotification(notification)

        return savedNotification
    }

}