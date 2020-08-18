package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ClassCouplingDto::class)
interface ClassCouplingDtoDao {
    @GetGeneratedKeys
    @SqlUpdate("insert into class_coupling (" +
            "class_id, project_id, inner_fan_in, inner_fan_out, outer_fan_in, " +
            "outer_fan_out, inner_instability, inner_coupling, outer_instability, outer_coupling) " +
            "values(:classId, :projectId, :innerFanIn, :innerFanOut, :outerFanIn, " +
            ":outerFanOut, :innerInstability, :innerCoupling, :outerInstability, :outerCoupling)")
    fun insert(@BindBean classCouplingDto: ClassCouplingDto): Long
}

data class ClassCouplingDto(val classId: String,
                            val projectId: Int,
                            val innerFanIn: Int,
                            val innerFanOut: Int,
                            val outerFanIn: Int,
                            val outerFanOut: Int,
                            val innerInstability: Double,
                            val innerCoupling: Double,
                            val outerInstability: Double,
                            val outerCoupling: Double) {
    companion object {
        fun fromClassCoupling(classCoupling: ClassCoupling): ClassCouplingDto {
            return ClassCouplingDto(classCoupling.jClassVO.id!!, 0, classCoupling.innerFanIn, classCoupling.innerFanOut,
                    classCoupling.outerFanIn, classCoupling.outerFanOut, classCoupling.innerInstability, classCoupling.innerCoupling,
                    classCoupling.outerInstability, classCoupling.outerCoupling)
        }
    }
}

