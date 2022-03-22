package com.thoughtworks.archguard.report.domain.githotfile

import com.thoughtworks.archguard.report.domain.container.ContainerDemand

interface ContainerServiceRepo {
    fun findBySystemId(systemId: Long) : List<ContainerDemand>
}