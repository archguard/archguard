package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerServiceResponse
import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import org.springframework.stereotype.Service

@Service
class ServicesMapService(val repo: ContainerServiceRepo) {
    fun findBySystemId(systemId: Long) : ContainerServiceResponse {
        return ContainerServiceResponse(
            id = systemId,
            demands = repo.findDemandBySystemId(systemId),
            resources = repo.findResourceBySystemId(systemId)
        )
    }

    fun allContainerServices(): List<ContainerServiceResponse> {
        val allSystems = repo.findAllSystemIdName()
        return allSystems.map {
            ContainerServiceResponse(
                id = it.id,
                name = it.systemName,
                demands = repo.findDemandBySystemId(it.id),
                resources = repo.findResourceBySystemId(it.id)
            )
        }.toList()
    }
}
