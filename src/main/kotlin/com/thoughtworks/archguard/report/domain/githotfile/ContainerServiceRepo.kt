package com.thoughtworks.archguard.report.domain.githotfile

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.container.ContainerResource
import com.thoughtworks.archguard.report.domain.container.ContainerServiceDO

interface ContainerServiceRepo {
    fun findDemandBySystemId(systemId: Long): List<ContainerDemand>
    fun findResourceBySystemId(systemId: Long): List<ContainerResource>
    fun findAllSystemIdName(): List<ContainerServiceDO>
}