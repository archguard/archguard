package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(MethodMetricPO::class)
interface MethodMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into method_metrics (" +
            "method_id, system_id, fanin, fanout) " +
            "values <values>")
    fun insert(@BindBeanList(value = "values", propertyNames = ["methodId", "systemId", "fanIn", "fanOut"]) methodMetricPOs: List<MethodMetricPO>): Long

    @SqlUpdate("DELETE FROM method_metrics where system_id = :systemId")
    fun deleteBy(@Bind("systemId") systemId: Long)
}