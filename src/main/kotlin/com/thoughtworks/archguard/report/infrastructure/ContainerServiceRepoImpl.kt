package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.container.ContainerResource
import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import org.springframework.stereotype.Repository

@Repository
class ContainerServiceRepoImpl(val dao: ContainerServiceDao) : ContainerServiceRepo {
    override fun findDemandBySystemId(systemId: Long): List<ContainerDemand> {
        return dao.findDemandBySystemId(systemId)
    }

    override fun findResourceBySystemId(systemId: Long): List<ContainerResource> {
        return dao.findResourceBySystemId(systemId)
    }
}
