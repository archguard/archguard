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
        return allSystems.map { system ->
            val demands = repo.findDemandBySystemId(system.id).map {
                it.targetUrl = updateUrl(it.targetUrl)
                it
            }.toList()
            val resources = repo.findResourceBySystemId(system.id).map {
                it.sourceUrl = updateUrl(it.sourceUrl)
                it
            }.toList()
            ContainerServiceResponse(
                id = system.id,
                name = system.systemName,
                demands = demands,
                resources = resources
            )
        }.toList()
    }

    fun updateUrl(targetUrl: String): String {
        return targetUrl.replace("\\$\\{[a-zA-Z]+\\}".toRegex(), "@uri@")
    }
}
