package org.archguard.scanner.analyser.api.postman;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PostmanAuthTest {

    @Test
    fun shouldReturnBearerFormat() {
        // given
        val auth = PostmanAuth()
        auth.type = "bearer"
        val variable1 = PostmanVariable().also {
            it.value = "token1"
        }
        val variable2 = PostmanVariable().also {
            it.value = "token2"
        }
        auth.bearer = listOf(variable1, variable2)

        // when
        val result = auth.format()

        // then
        assertEquals("Bearer null=token1,null=token2", result)
    }

    @Test
    fun shouldReturnOAuth2Format() {
        // given
        val auth = PostmanAuth()
        auth.type = "oauth2"
        val variable1 = PostmanVariable().also {
            it.key = "key1"
            it.value = "token1"
        }
        val variable2 = PostmanVariable().also {
            it.value = "token2"
        }
        auth.oauth2 = listOf(variable1, variable2)

        // when
        val result = auth.format()

        // then
        assertEquals("OAuth2 key1=token1,null=token2", result)
    }

    @Test
    fun shouldReturnDefaultFormat() {
        // given
        val auth = PostmanAuth()
        auth.type = "unknown"

        // when
        val result = auth.format()

        // then
        assertEquals("type unknown", result)
    }
}
