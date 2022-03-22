package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import org.springframework.stereotype.Repository

@Repository
class ContainerServiceRepoImpl(val dao: ContainerServiceDao) : ContainerServiceRepo {
    override fun findBySystemId(systemId: Long): List<ContainerDemand> {
        return dao.findBySystemId(systemId)
    }
}
