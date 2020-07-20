package com.thoughtworks.archguard.module.infrastructure.metrics

import com.thoughtworks.archguard.module.domain.metrics.ClassMetrics
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ClassMetrics::class)
interface ClassMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into metrics_class (" +
            "package_id, class_name, inner_fan_in, inner_fan_out, outer_fan_in, " +
            "outer_fan_out, inner_instability, inner_coupling, outer_instability, outer_coupling) " +
            "values(:packageId, :className, :innerFanIn, :innerFanOut, :outerFanIn, " +
            ":outerFanOut, :innerInstability, :innerCoupling, :outerInstability, :outerCoupling)")
    fun insert(@BindBean classMetrics: ClassMetrics): Long
}