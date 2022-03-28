package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerServiceDO
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

    fun findAllServiceByIds(ids: List<String>): List<ContainerServiceResponse> {
        val allSystems = repo.findSystems(ids)
        return calculateServiceByIds(allSystems)
    }


    fun allContainerServices(): List<ContainerServiceResponse> {
        val allSystems = repo.findAllSystemIdName()
        return calculateServiceByIds(allSystems)
    }

    private fun calculateServiceByIds(allSystems: List<ContainerServiceDO>): List<ContainerServiceResponse> {
        return allSystems.map { system ->
            val demands = repo.findDemandBySystemId(system.id).map {
                it.originUrl = it.targetUrl
                it.targetUrl = updateUrl(it.targetUrl)
                it
            }.toList()
            val resources = repo.findResourceBySystemId(system.id).map {
                it.originUrl = it.sourceUrl
                it.sourceUrl = updateUrl(it.sourceUrl)
                it
            }.toList()
            ContainerServiceResponse(
                id = system.id,
                language = system.language,
                name = system.systemName,
                demands = demands,
                resources = resources
            )
        }.toList()
    }

    // todo: without query in url like: ?language=@uri@
    fun updateUrl(targetUrl: String): String {
        return targetUrl.replace("\\$\\{[a-zA-Z.]+\\}".toRegex(), "@uri@")
            .replace("\\{[a-zA-Z.]+\\}".toRegex(), "@uri@")
    }
}
