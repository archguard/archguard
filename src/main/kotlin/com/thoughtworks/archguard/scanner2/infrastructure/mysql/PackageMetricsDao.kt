package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.PackageMetric
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(PackageMetric::class)
interface PackageMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate(
        "insert into metric_package (" +
            "module_name, package_name, system_id, fanin, fanout) " +
            "values <values>"
    )
    fun insert(@BindBeanList(value = "values", propertyNames = ["moduleName", "packageName", "systemId", "fanIn", "fanOut"]) packageMetrics: List<PackageMetric>): Long

    @SqlUpdate("DELETE FROM metric_package where system_id = :systemId")
    fun deleteBy(@Bind("systemId") systemId: Long)
}
