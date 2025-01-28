package com.security.app.model

enum class Language(val value: String) {
    VIETNAMESE("VIETNAMESE"),
    ENGLISH("ENGLISH"),
    FRENCH("FRENCH");

    companion object {
        fun fromString(value: String): Language {
            return when (value) {
                "VIETNAMESE" -> VIETNAMESE
                "ENGLISH" -> ENGLISH
                "FRENCH" -> FRENCH
                else -> throw IllegalArgumentException("Language not found")
            }
        }
    }
}

enum class NotificationStatus(val value: String) {
    PENDING("PENDING"),
    SENT("SENT"),
    FAILED("FAILED");

    companion object {
        fun fromString(value: String): NotificationStatus {
            return when (value) {
                "PENDING" -> PENDING
                "SENT" -> SENT
                "FAILED" -> FAILED
                else -> throw IllegalArgumentException("Notification status not found")
            }
        }
    }
}