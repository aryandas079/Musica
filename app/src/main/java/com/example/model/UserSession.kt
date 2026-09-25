package com.example.model

data class UserSession(
    val uid: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val isAnonymous: Boolean = false,
    val authProvider: String = "Google Sign-In"
) {
    val greetingName: String
        get() {
            if (!displayName.isNullOrBlank()) {
                return displayName.trim().split(" ").firstOrNull() ?: displayName.trim()
            }
            if (!email.isNullOrBlank()) {
                val userPart = email.substringBefore("@")
                val cleanPart = userPart.split(".", "_", "-").firstOrNull()?.replaceFirstChar { it.uppercase() }
                return cleanPart ?: userPart
            }
            return "Music Lover"
        }

    val avatarLetter: String
        get() = greetingName.firstOrNull()?.uppercase() ?: "M"
}

enum class SyncStatus {
    IDLE,
    SYNCING,
    SYNCED,
    ERROR
}
