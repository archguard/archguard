package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerServiceDO
import com.thoughtworks.archguard.report.domain.container.ContainerServiceRepo
import com.thoughtworks.archguard.report.domain.container.ContainerServiceResponse
import org.springframework.stereotype.Service

@Service
class ServicesMapService(val repo: ContainerServiceRepo) {
    fun findBySystemId(systemId: Long): ContainerServiceResponse {
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

    private val FRONTEND_PARA = "\\$\\{[a-zA-Z.]+\\}".toRegex()
    private val SPRING_PARA = "\\{[a-zA-Z.]+\\}".toRegex()
    // such as: `$baseUrl/api/quality-gate-profile/$qualityGateName`
    private val KOTLIN_LINE_STR_REF = "\\$[a-zA-Z]+".toRegex()

    // todo: without query in url like: ?language=@uri@
    fun updateUrl(targetUrl: String): String {
        return targetUrl
            .replace(FRONTEND_PARA, "@uri@")
            .replace(KOTLIN_LINE_STR_REF, "@uri@")
            .replace(SPRING_PARA, "@uri@")
    }
}
