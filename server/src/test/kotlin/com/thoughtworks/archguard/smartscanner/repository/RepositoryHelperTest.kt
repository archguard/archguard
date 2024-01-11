package com.thoughtworks.archguard.smartscanner.repository;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RepositoryHelperTest {

    @Test
    fun shouldGenerateId() {
        // given
        val expectedId = UUID.randomUUID().toString()

        // when
        val actualId = RepositoryHelper.generateId()

        // then
        assertEquals(expectedId, actualId)
    }

    @Test
    fun shouldGetCurrentTime() {
        // given
        val expectedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        // when
        val actualTime = RepositoryHelper.getCurrentTime()

        // then
        assertEquals(expectedTime, actualTime)
    }
}
