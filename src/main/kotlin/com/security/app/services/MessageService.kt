package com.security.app.services

import com.security.app.entities.Notification
import com.security.app.entities.UserNotificationCredential
import com.security.app.model.FcmNotificationModel
import com.security.app.model.MailNotificationModel
import com.security.app.model.NotificationStatus
import com.security.app.model.SmsNotificationModel
import com.security.app.request.NotificationSendCallback
import com.security.app.request.SendNotificationRequest
import com.security.app.request.UpdateUserCredentialRequest
import com.security.app.utils.*
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
    private val fcmTemplateService: FcmTemplateService,
    private val fcmTypeHandler: FcmTypeHandler,
) {
    private final val queueUrl = System.getenv("SQS_URL")

    fun sendMessage(message: SendNotificationRequest): List<Notification>? {

        val requestList = mutableListOf<SendMessageRequest>()
        val notificationsList = mutableListOf<Notification>()

        /// This is for mail notification
        var mailNotificationModel: MailNotificationModel? = null
        if (message.channels.contains("mail")) {
            val mailNotification = handleMailNotification(message)
            if (!mailNotification.isNullOrEmpty()) {
                for (mailNoti in mailNotification) {
                    notificationsList.add(mailNoti)
                    mailNotificationModel =
                        mailTypeHandler.handleMailType(mailNoti.templateType, mailNoti, message.message)

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
                }
            } else {
                return null
            }
        }

        /// This is for sms notification
        var smsNotificationModel: SmsNotificationModel? = null
        if (message.channels.contains("sms")) {
            val smsNotification = handleSmsNotification(message)
            if (!smsNotification.isNullOrEmpty()) {
                for (smsNoti in smsNotification) {
                    notificationsList.add(smsNoti)
                    smsNotificationModel =
                        smsTypeHandler.handleSmsType(smsNoti.templateType, smsNoti, message.message)

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
                }
            } else {
                return null
            }
        }

        /// This is for fcm notification
        var fcmNotificationModels: List<FcmNotificationModel> = emptyList()
        if (message.channels.contains("fcm")) {
            val fcmNotifications = handleFcmNotification(message)
            if (!fcmNotifications.isNullOrEmpty()) {
                for (fcmNoti in fcmNotifications) {
                    notificationsList.add(fcmNoti)
                    fcmNotificationModels =
                        fcmTypeHandler.handleFcmType(
                            fcmNoti.templateType,
                            fcmNoti,
                            message.message
                        )

                    for (fcmNotificationModel in fcmNotificationModels) {
                        val request = SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(jsonUtils.toJson(fcmNotificationModel))
                            .messageAttributes(
                                mapOf(
                                    "channel" to MessageAttributeValue.builder()
                                        .dataType("String")
                                        .stringValue("fcm")
                                        .build(),
                                )
                            )
                        requestList.add(request.build())
                    }
                }
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

    fun updateUserNotificationCredential(request: UpdateUserCredentialRequest): UserNotificationCredential {
        val userNotificationCredential = userNotificationCredentialService.updateUserCredential(request)
        return userNotificationCredential
    }

    private fun handleMailNotification(message: SendNotificationRequest): List<Notification>? {
        val mailNotificationTemplate = mailNotificationTemplateService.getTemplateByType(message.notificationType)
            ?: return null

        val userCredential =
            userNotificationCredentialService.getCredentialsByUserId(message.receiverId) ?: return null

        /// Make user credentials list distinct of email

        val notificationList = mutableListOf<Notification>()
        val notification = Notification().let {
            it.mailNotificationTemplate = mailNotificationTemplate
            it.status = NotificationStatus.PENDING
            it.templateType = message.notificationType
            it.userNotificationCredential = userCredential
            it
        }
        notificationList.add(notification)

        val savedNotification = notificationService.saveAllNotifications(notificationList)

        return savedNotification
    }

    private fun handleSmsNotification(message: SendNotificationRequest): List<Notification>? {
        val mailNotificationTemplate = smsNotificationTemplateService.getTemplateByType(message.notificationType)
            ?: return null

        val userCredential = userNotificationCredentialService.getCredentialsByUserId(message.receiverId) ?: return null

        val notificationList = mutableListOf<Notification>()

        val notification = Notification().let {
            it.smsNotificationTemplate = mailNotificationTemplate
            it.status = NotificationStatus.PENDING
            it.templateType = message.notificationType
            it.userNotificationCredential = userCredential
            it
        }
        notificationList.add(notification)

        val savedNotification = notificationService.saveAllNotifications(notificationList)

        return savedNotification
    }

    private fun handleFcmNotification(message: SendNotificationRequest): List<Notification>? {
        val fcmNotificationTemplate = fcmTemplateService.getTemplateByType(message.notificationType)
            ?: return null

        val userCredential = userNotificationCredentialService.getCredentialsByUserId(message.receiverId) ?: return null

        val notificationList = mutableListOf<Notification>()
        val notification = Notification().let {
            it.pushNotificationTemplate = fcmNotificationTemplate
            it.status = NotificationStatus.PENDING
            it.templateType = message.notificationType
            it.userNotificationCredential = userCredential
            it
        }
        notificationList.add(notification)

        val savedNotification = notificationService.saveAllNotifications(notificationList)

        return savedNotification
    }

    fun handleNotification(notificationId: String, callbackRequest: NotificationSendCallback): Notification {
        val statusEnum = NotificationStatus.fromString(callbackRequest.status)
        val notification = notificationService.getNotificationById(notificationId.toUUID())
            ?: throw IllegalArgumentException("Notification not found")

        if (statusEnum == NotificationStatus.FAILED && callbackRequest.channel == "fcm") {
            removeFcmTokenOfUser(notification.userNotificationCredential, callbackRequest.fcmToken)
        }

        notification.status = statusEnum

        return notificationService.saveNotification(notification)
    }

    private fun removeFcmTokenOfUser(
        userNotificationCredential: UserNotificationCredential,
        fcmToken: String?
    ): UserNotificationCredential {
        val fcmTokenList: MutableList<String> =
            jsonUtils.fromJson(userNotificationCredential.userFcmToken, List::class.java).mapNotNull { it as? String }
                .toMutableList()

        if (fcmTokenList.contains(fcmToken)) {
            fcmTokenList.remove(fcmToken)
        }

        userNotificationCredential.userFcmToken = jsonUtils.toJson(fcmTokenList)

        return userNotificationCredentialService.updateUserCredential(userNotificationCredential)
    }
}