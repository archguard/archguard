package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.ModuleMetric
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBeanList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ModuleMetric::class)
interface ModuleMetricsDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into module_metrics (" +
            "module_name, system_id, fanin, fanout) " +
            "values <values>")
    fun insert(@BindBeanList(value = "values", propertyNames = ["moduleName", "systemId", "fanIn", "fanOut"]) moduleMetrics: List<ModuleMetric>): Long

    @SqlUpdate("DELETE FROM module_metrics where system_id = :systemId")
    fun deleteBy(@Bind("systemId") systemId: Long)
}
