package com.thoughtworks.archguard.metrics.infrastructure

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.SqlQuery

@RegisterBeanMapper(ClassCouplingReadPO::class)
interface ClassCouplingReadDao {
    @SqlQuery("select c.class_id, c.system_id, j.name, j.module, " +
            "c.inner_fan_in, c.inner_fan_out, c.outer_fan_in, c.outer_fan_out " +
            "from class_coupling c join JClass j where c.class_id = j.id and j.id = :classId")
    fun findClassCoupling(@Bind("classId") classId: String): ClassCouplingReadPO?

    @SqlQuery("select c.class_id, c.system_id, j.name, j.module, " +
            "c.inner_fan_in, c.inner_fan_out, c.outer_fan_in, c.outer_fan_out " +
            "from class_coupling c join JClass j where c.class_id = j.id and j.id in (<classIds>)")
    fun findClassCouplings(@BindList("classIds") classIds: List<String>): List<ClassCouplingReadPO>
}


