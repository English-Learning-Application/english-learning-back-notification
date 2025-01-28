package com.security.app.repositories

import com.security.app.entities.UserNotificationCredential
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserNotificationCredentialsRepository : JpaRepository<UserNotificationCredential, UUID> {
    fun findAllByUserId(userId: UUID): List<UserNotificationCredential>
}