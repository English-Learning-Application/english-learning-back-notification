package com.security.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
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
class UserNotificationCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(nullable = false)
    var userId: UUID = UUID.randomUUID()

    @Column(nullable = false, columnDefinition = "TEXT")
    var userEmailAddress: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var userPhoneNumber: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var userFcmToken: String = ""

    @Column(nullable = false)
    var username: String = ""

    @OneToMany(mappedBy = "userNotificationCredential", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    var notifications: List<Notification> = mutableListOf()

    @Column(nullable = true, updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
}