package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetricsLegacy
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ModuleMetricsLegacy::class)
interface ModuleMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into metrics_module (" +
            "module_name, outer_instability_avg, outer_instability_med, outer_coupling_avg, outer_coupling_med, " +
            "inner_instability_avg, inner_instability_med, inner_coupling_avg, inner_coupling_med) " +
            "values(:moduleName, :outerInstabilityAvg, :outerInstabilityMed, :outerCouplingAvg, :outerCouplingMed," +
            ":innerInstabilityAvg, :innerInstabilityMed, :innerCouplingAvg, :innerCouplingMed)")
    fun insert(@BindBean moduleMetrics: ModuleMetricsLegacy): Long

    @SqlQuery("select m.* from metrics_module m where m.module_name = :moduleName")
    fun findModuleMetricsByModuleName(@Bind("moduleName") moduleName: String): ModuleMetricsLegacy

    @SqlUpdate("TRUNCATE TABLE metrics_module")
    fun truncate()
}