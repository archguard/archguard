package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import org.springframework.stereotype.Service

@Service
class ServicesMapService(val repo: ContainerServiceRepo) {
    fun findBySystemId(systemId: Long) : List<ContainerDemand> {
        return repo.findBySystemId(systemId)
    }
}
