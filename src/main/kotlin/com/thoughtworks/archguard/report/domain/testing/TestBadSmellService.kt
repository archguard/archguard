package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestBadSmellService(val testBadSmellRepository: TestBadSmellRepository) {

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
        val unassertTestMethodIds = testBadSmellRepository.getUnassertTestMethodCount(systemId)
        return if (unassertTestMethodIds.isEmpty()) {
            MethodInfoListDTO(Collections.emptyList(), 0, offset / limit + 1)
        } else {
            val unassertMethodCount = unassertTestMethodIds.size.toLong()
            val unassertignoreMethods = testBadSmellRepository.getUnassertTestMethods(unassertTestMethodIds, limit, offset)
            MethodInfoListDTO(unassertignoreMethods, unassertMethodCount, offset / limit + 1)
        }
    }

}
