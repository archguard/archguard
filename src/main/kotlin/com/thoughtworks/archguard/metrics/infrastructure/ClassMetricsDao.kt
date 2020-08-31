package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassMetricPO
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ClassMetricPO::class)
interface ClassMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into class_metrics (" +
            "class_id, project_id, abc, noc, dit, lcom4) " +
            "values <values>")
    fun insert(@BindBeanList(value = "values", propertyNames = ["classId", "systemId", "abc", "noc", "dit", "lcom4"]) classMetricPOs: List<ClassMetricPO>): Long

    @SqlUpdate("DELETE FROM class_metrics where project_id = :systemId")
    fun deleteBy(@Bind("systemId") systemId: Long)
}
