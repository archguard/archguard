package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerServiceResponse
import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import org.springframework.stereotype.Service

@Service
class ServicesMapService(val repo: ContainerServiceRepo) {
    fun findBySystemId(systemId: Long) : ContainerServiceResponse {
        return ContainerServiceResponse(
            demands = repo.findDemandBySystemId(systemId),
            resources = repo.findResourceBySystemId(systemId)
        )
    }

    fun allContainer(): ContainerServiceResponse {
        val containerServiceResponse = ContainerServiceResponse(
            demands = repo.findDemandBySystemId(1),
            resources = repo.findResourceBySystemId(1)
        )
        return containerServiceResponse
    }
}
