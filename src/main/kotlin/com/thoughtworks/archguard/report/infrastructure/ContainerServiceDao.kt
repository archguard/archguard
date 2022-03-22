package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ContainerServiceDao {
    @SqlQuery("select source_package as sourcePackage, source_class as sourceClass, source_method as sourceMethod," +
            " target_url as targetUrl, target_http_method as targetHttpMethod" +
            " from container_demand where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<ContainerDemand>
}
