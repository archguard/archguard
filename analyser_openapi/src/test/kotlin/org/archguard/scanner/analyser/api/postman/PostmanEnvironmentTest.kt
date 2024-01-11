package org.archguard.scanner.analyser.api.postman;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PostmanEnvironmentTest {

    @Test
    fun shouldInitializeLookupMap() {
        // given
        val environment = PostmanEnvironment()
        val values = listOf(
            PostmanEnvValue().also {
                it.key = "key1"
                it.value = "value1"
            },
//            PostmanEnvValue("key2", "value2"),
            PostmanEnvValue().also {
                it.key = "key2"
                it.value = "value2"
            },
//            PostmanEnvValue("key3", "value3")
            PostmanEnvValue().also {
                it.key = "key3"
                it.value = "value3"
            }
        )
        environment.values = values

        // when
        environment.init()

        // then
        assertEquals(values.size, environment.lookup.size)
        for (value in values) {
            assertEquals(value, environment.lookup[value.key])
        }
    }

    @Test
    fun shouldUpdateExistingEnvironmentVariable() {
        // given
        val environment = PostmanEnvironment()
        val existingKey = "existingKey"
        val existingValue = "existingValue"
        val newValue = "newValue"
        val existingVar = PostmanEnvValue().also {
            it.key = existingKey
            it.value = existingValue
        }
        environment.lookup[existingKey] = existingVar

        // when
        environment.setEnvironmentVariable(existingKey, newValue)

        // then
        assertEquals(newValue, existingVar.value)
    }

    @Test
    fun shouldCreateNewEnvironmentVariable() {
        // given
        val environment = PostmanEnvironment()
        val newKey = "newKey"
        val newValue = "newValue"

        // when
        environment.setEnvironmentVariable(newKey, newValue)

        // then
        val newVar = environment.lookup[newKey]
        assertEquals(newKey, newVar?.key)
        assertEquals(newValue, newVar?.value)
    }
}
