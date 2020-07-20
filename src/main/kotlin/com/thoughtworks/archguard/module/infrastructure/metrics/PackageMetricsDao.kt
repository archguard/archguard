package com.thoughtworks.archguard.module.infrastructure.metrics

import com.thoughtworks.archguard.module.domain.metrics.PackageMetrics
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(PackageMetrics::class)
interface PackageMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into metrics_package (" +
            "module_id, package_name, outer_instability_avg, outer_instability_med, outer_coupling_avg, " +
            "outer_coupling_med, inner_instability_avg, inner_instability_med, inner_coupling_avg, inner_coupling_med) " +
            "values(:moduleId, :packageName, :outerInstabilityAvg, :outerInstabilityMed, :outerCouplingAvg, " +
            ":outerCouplingMed, :innerInstabilityAvg, :innerInstabilityMed, :innerCouplingAvg, :innerCouplingMed)")
    fun insert(@BindBean packageMetrics: PackageMetrics): Long
}