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
class PushNotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(nullable = false)
    var templateType: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var title: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var body: String = ""

    @Column(nullable = true)
    var action: String? = null

    @Column(nullable = false)
    var isActive: Boolean = true

    @OneToMany(mappedBy = "pushNotificationTemplate", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonIgnore
    var notifications: List<Notification> = mutableListOf()

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}