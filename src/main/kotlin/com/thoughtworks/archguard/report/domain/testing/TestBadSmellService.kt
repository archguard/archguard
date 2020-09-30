package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import org.springframework.stereotype.Service

@Service
class TestBadSmellService(val testBadSmellRepository: TestBadSmellRepository) {

    fun getStaticMethodList(systemId: Long, limit: Long, offset: Long): StaticMethodListDto {
        ValidPagingParam.validPagingParam(limit, offset)
        val staticMethodCount = testBadSmellRepository.getStaticMethodCount(systemId)
        val staticMethods = testBadSmellRepository.getStaticMethods(systemId, limit, offset)
        return StaticMethodListDto(staticMethods, staticMethodCount, offset / limit + 1)
    }

}
