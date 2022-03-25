package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.container.ContainerResource
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface ContainerServiceDao {
    @SqlQuery("select source_package as sourcePackage, source_class as sourceClass, source_method as sourceMethod," +
            " target_url as targetUrl, target_http_method as targetHttpMethod, system_id as systemId" +
            " from container_demand where system_id = :systemId")
    fun findDemandBySystemId(systemId: Long) : List<ContainerDemand>

    @SqlQuery("select package_name as packageName, class_name as className, method_name as methodName," +
            " source_url as sourceUrl, source_http_method as sourceHttpMethod, system_id as systemId" +
            " from container_resource where system_id = :systemId")
    fun findResourceBySystemId(systemId: Long): List<ContainerResource>

    @SqlQuery("select package_name as packageName, class_name as className, method_name as methodName," +
            " source_url as sourceUrl, source_http_method as sourceHttpMethod, system_id as systemId" +
            " from container_resource")
    fun findAllResources(): List<ContainerResource>

    @SqlQuery("select source_package as sourcePackage, source_class as sourceClass, source_method as sourceMethod," +
            " target_url as targetUrl, target_http_method as targetHttpMethod, system_id as systemId")
    fun findAllDemands(): List<ContainerDemand>
}
