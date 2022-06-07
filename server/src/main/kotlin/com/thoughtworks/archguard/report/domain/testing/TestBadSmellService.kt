package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.springframework.stereotype.Service

@Service
class TestBadSmellService(val testBadSmellRepository: TestBadSmellRepository) {
    fun getStaticMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val staticMethodCount = testBadSmellRepository.getStaticMethodCount(systemId)
        val staticMethods = testBadSmellRepository.getStaticMethods(systemId, limit, offset)
        return MethodInfoListDTO(staticMethods, staticMethodCount, offset / limit + 1)
    }

    fun getSleepTestMethodList(systemId: Long, limit: Long, offset: Long): IssueListDTO {
        return issueListDTO(limit, offset, systemId, "SleepyTest")
    }

    private fun issueListDTO(
        limit: Long,
        offset: Long,
        systemId: Long,
        type: String,
    ): IssueListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val sleepMethodCount = testBadSmellRepository.countTestSmellByType(systemId, type)
        val sleepMethods = testBadSmellRepository.getTestSmellByType(systemId, type, limit, offset)
        return IssueListDTO(sleepMethods, sleepMethodCount, offset / limit + 1)
    }

    fun getEmptyTestMethodList(systemId: Long, limit: Long, offset: Long): IssueListDTO {
        return issueListDTO(limit, offset, systemId, "EmptyTest")
    }

    fun getIgnoreTestMethodList(systemId: Long, limit: Long, offset: Long): IssueListDTO {
        return issueListDTO(limit, offset, systemId, "IgnoreTest")
    }

    fun getUnassertTestMethodList(systemId: Long, limit: Long, offset: Long): IssueListDTO {
        return issueListDTO(limit, offset, systemId, "UnknownTest")
    }

    fun getMultiAssertTestMethodList(systemId: Long, limit: Long, offset: Long): IssueListDTO {
        return issueListDTO(limit, offset, systemId, "DuplicateAssertTest")
    }

    fun getRedundantPrintTestMethodList(systemId: Long, limit: Long, offset: Long): IssueListDTO {
        return issueListDTO(limit, offset, systemId, "RedundantPrint")
    }

    fun getTestingReport(systemId: Long): Map<BadSmellType, Long> {
        val ignoreTestMethodCount = testBadSmellRepository.countTestSmellByType(systemId, "IgnoreTest")
        val sleepTestMethodCount = testBadSmellRepository.countTestSmellByType(systemId, "SleepyTest")
        val unAssertTestCount = testBadSmellRepository.countTestSmellByType(systemId, "UnknownTest")

        return mapOf(
            (BadSmellType.IGNORE_TEST to ignoreTestMethodCount),
            (BadSmellType.SLEEP_TEST to sleepTestMethodCount),
            (BadSmellType.UN_ASSERT_TEST to unAssertTestCount)
        )
    }
}
