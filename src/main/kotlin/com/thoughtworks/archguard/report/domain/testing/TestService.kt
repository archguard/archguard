package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import org.springframework.stereotype.Service

@Service
class TestService(val testingRepository: TestingRepository) {

    fun getStaticMethodList(systemId: Long, limit: Long, offset: Long): StaticMethodListDto {
        ValidPagingParam.validPagingParam(limit, offset)
        val staticMethodCount = testingRepository.getStaticMethodCount(systemId)
        val staticMethods = testingRepository.getStaticMethods(systemId, limit, offset)
        return StaticMethodListDto(staticMethods, staticMethodCount, offset / limit + 1)
    }

}
