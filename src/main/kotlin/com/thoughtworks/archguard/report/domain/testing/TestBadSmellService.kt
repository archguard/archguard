package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestBadSmellService(val testBadSmellRepository: TestBadSmellRepository) {

    @Value("\${threshold.test-bad-smell.redundant-print}")
    private val redundantPrintThreshold: Int = 0

    @Value("\${threshold.test-bad-smell.multi-assert}")
    private val multiAssertThreshold: Int = 0

    fun getStaticMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val staticMethodCount = testBadSmellRepository.getStaticMethodCount(systemId)
        val staticMethods = testBadSmellRepository.getStaticMethods(systemId, limit, offset)
        return MethodInfoListDTO(staticMethods, staticMethodCount, offset / limit + 1)
    }

    fun getSleepTestMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val sleepMethodCount = testBadSmellRepository.getSleepTestMethodCount(systemId)
        val sleepMethods = testBadSmellRepository.getSleepTestMethods(systemId, limit, offset)
        return MethodInfoListDTO(sleepMethods, sleepMethodCount, offset / limit + 1)
    }

    fun getEmptyTestMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val emptyMethodCount = testBadSmellRepository.getEmptyTestMethodCount(systemId)
        val emptyMethods = testBadSmellRepository.getEmptyTestMethods(systemId, limit, offset)
        return MethodInfoListDTO(emptyMethods, emptyMethodCount, offset / limit + 1)
    }

    fun getIgnoreTestMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val ignoreMethodCount = testBadSmellRepository.getIgnoreTestMethodCount(systemId)
        val ignoreMethods = testBadSmellRepository.getIgnoreTestMethods(systemId, limit, offset)
        return MethodInfoListDTO(ignoreMethods, ignoreMethodCount, offset / limit + 1)
    }

    fun getUnassertTestMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val unassertTestMethodIds = testBadSmellRepository.getUnassertTestMethodIds(systemId)
        return if (unassertTestMethodIds.isEmpty()) {
            MethodInfoListDTO(Collections.emptyList(), 0, offset / limit + 1)
        } else {
            val unassertMethodCount = unassertTestMethodIds.size.toLong()
            val unassertignoreMethods = testBadSmellRepository.getMethodsByIds(unassertTestMethodIds, limit, offset)
            MethodInfoListDTO(unassertignoreMethods, unassertMethodCount, offset / limit + 1)
        }
    }

    fun getMultiAssertTestMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val multiAssertMethodIds = testBadSmellRepository.getAssertMethodAboveThresholdIds(systemId, multiAssertThreshold)
        return if (multiAssertMethodIds.isEmpty()) {
            MethodInfoListDTO(Collections.emptyList(), 0, offset / limit + 1)
        } else {
            val multiAssertMethods = testBadSmellRepository.getMethodsByIds(multiAssertMethodIds, limit, offset)
            MethodInfoListDTO(multiAssertMethods, multiAssertMethodIds.size.toLong(), offset / limit + 1)
        }
    }

    fun getRedundantPrintTestMethodList(systemId: Long, limit: Long, offset: Long): MethodInfoListDTO {
        ValidPagingParam.validPagingParam(limit, offset)
        val redundantPrintMethodIds = testBadSmellRepository.getRedundantPrintAboveThresholdIds(systemId, redundantPrintThreshold)
        return if (redundantPrintMethodIds.isEmpty()) {
            MethodInfoListDTO(Collections.emptyList(), 0, offset / limit + 1)
        } else {
            val redundantPrintMethods = testBadSmellRepository.getMethodsByIds(redundantPrintMethodIds, limit, offset)
            MethodInfoListDTO(redundantPrintMethods, redundantPrintMethodIds.size.toLong(), offset / limit + 1)
        }
    }

    fun getTestingReport(systemId: Long): Map<BadSmellType, Long> {
        val ignoreTestMethodCount = testBadSmellRepository.getIgnoreTestMethodCount(systemId)
        val sleepTestMethodCount = testBadSmellRepository.getSleepTestMethodCount(systemId)
        val unAssertTestCount = testBadSmellRepository.getUnassertTestMethodIds(systemId).size.toLong()

        return mapOf(
                (BadSmellType.IGNORE_TEST to ignoreTestMethodCount),
                (BadSmellType.SLEEP_TEST to sleepTestMethodCount),
                (BadSmellType.UN_ASSERT_TEST to unAssertTestCount))
    }

}
