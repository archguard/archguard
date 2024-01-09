package com.thoughtworks.archguard.report.domain.testing;

import com.thoughtworks.archguard.report.domain.testing.IssueListDTO
import com.thoughtworks.archguard.report.domain.testing.MethodInfoListDTO
import com.thoughtworks.archguard.report.domain.testing.TestBadSmellRepository
import com.thoughtworks.archguard.report.domain.testing.TestBadSmellService
import com.thoughtworks.archguard.report.infrastructure.TestSmellPO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class TestBadSmellServiceTest {

    private val testBadSmellRepository = mock(TestBadSmellRepository::class.java)
    private val testBadSmellService = TestBadSmellService(testBadSmellRepository)

    @Test
    fun shouldReturnStaticMethodList() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val staticMethodCount = 5L
        val staticMethods = listOf(
            MethodInfo("1",  1,  null, "org.archguard", "public", "method1"),
            MethodInfo("2",  1,  null, "org.archguard", "public", "method2")
        )
        `when`(testBadSmellRepository.getStaticMethodCount(systemId)).thenReturn(staticMethodCount)
        `when`(testBadSmellRepository.getStaticMethods(systemId, limit, offset)).thenReturn(staticMethods)

        // when
        val result = testBadSmellService.getStaticMethodList(systemId, limit, offset)

        // then
        assertEquals(staticMethods, result.data)
        assertEquals(staticMethodCount, result.count)
        assertEquals(1, result.currentPageNumber)
    }

    @Test
    fun shouldReturnSleepTestMethodList() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val sleepMethodCount = 3L
        val sleepMethods = listOf(
            TestSmellPO("SleepyTest", detail = "", position = ""),
            TestSmellPO("SleepyTest", detail = "", position = "")
        )
        `when`(testBadSmellRepository.countTestSmellByType(systemId, "SleepyTest")).thenReturn(sleepMethodCount)
        `when`(testBadSmellRepository.getTestSmellByType(systemId, "SleepyTest", limit, offset)).thenReturn(sleepMethods)

        // when
        val result = testBadSmellService.getSleepTestMethodList(systemId, limit, offset)

        // then
        assertEquals(sleepMethods, result.data)
        assertEquals(sleepMethodCount, result.count)
        assertEquals(1, result.currentPageNumber)
    }
}
