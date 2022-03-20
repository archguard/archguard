package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ClassMetricPO::class)
interface ClassMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into metrics_class (" +
            "class_id, system_id, noc, dit, lcom4, fanin, fanout) " +
            "values <values>")
    fun insert(@BindBeanList(value = "values", propertyNames = ["classId", "systemId", "noc", "dit", "lcom4", "fanIn", "fanOut"]) classMetricPOs: List<ClassMetricPO>): Long

    @SqlUpdate("DELETE FROM metrics_class where system_id = :systemId")
    fun deleteBy(@Bind("systemId") systemId: Long)
}
