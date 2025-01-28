package com.security.app.entities

import com.security.app.model.NotificationStatus
import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EntityListeners(AuditingEntityListener::class)
@Table
class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(nullable = false)
    var templateType: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_notification_credential_id", nullable = false)
    lateinit var userNotificationCredential: UserNotificationCredential

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_notification_template_id", nullable = true)
    var mailNotificationTemplate: MailNotificationTemplate? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sms_notification_template_id", nullable = true)
    var smsNotificationTemplate: SmsNotificationTemplate? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "push_notification_template_id", nullable = true)
    var pushNotificationTemplate: PushNotificationTemplate? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: NotificationStatus = NotificationStatus.PENDING

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}