package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerService
import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import org.springframework.stereotype.Service

@Service
class ServicesMapService(val repo: ContainerServiceRepo) {
    fun findBySystemId(systemId: Long) : ContainerService {
        return ContainerService(
            demands = repo.findDemandBySystemId(systemId),
            resources = repo.findResourceBySystemId(systemId)
        )
    }
}
