package com.example

import com.example.model.UserSession
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun userSession_greetingName_extractsDisplayNameFirstName() {
        val user = UserSession(
            uid = "user_123",
            email = "aryandas.dev@gmail.com",
            displayName = "Aryan Das"
        )
        assertEquals("Aryan", user.greetingName)
        assertEquals("A", user.avatarLetter)
    }

    @Test
    fun userSession_greetingName_fallsBackToEmailName() {
        val user = UserSession(
            uid = "user_456",
            email = "aryandas.dev@gmail.com",
            displayName = null
        )
        assertEquals("Aryandas", user.greetingName)
    }

    @Test
    fun userSession_anonymous_greetingFallback() {
        val user = UserSession(
            uid = "user_anon",
            email = null,
            displayName = null,
            isAnonymous = true
        )
        assertEquals("Music Lover", user.greetingName)
        assertEquals("M", user.avatarLetter)
    }
}
