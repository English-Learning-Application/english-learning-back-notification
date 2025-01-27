package com.security.app.services

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.SendMessageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class MessageService(
    private val amazonSQS: AmazonSQS
) {
    private final val queueUrl = System.getenv("SQS_URL")

    fun sendInstantMessage(message: String) {
        val request = SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(message)

        amazonSQS.sendMessage(request)
        println("Instant Message Sent: $message")
    }

    fun sendScheduledMessageAtSpecificTime(message: String, targetTime: LocalDateTime) {
        val now = LocalDateTime.now()
        val delaySeconds = ChronoUnit.SECONDS.between(now, targetTime)

        if (delaySeconds < 0) {
            throw IllegalArgumentException("The target time cannot be in the past")
        }

        val request = SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(message)
            .withDelaySeconds(delaySeconds.toInt())

        amazonSQS.sendMessage(request)
        println("Scheduled Message Sent: $message for $targetTime with $delaySeconds seconds delay")
    }

    fun sendMessageForSpecificTime() {
        val tomorrowAt7PM = LocalDateTime.now().plusDays(1).withHour(19).withMinute(0).withSecond(0)
        sendScheduledMessageAtSpecificTime("This is a scheduled message for tomorrow at 7 PM", tomorrowAt7PM)
    }
}