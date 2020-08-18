package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.customizer.BindList
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(ClassCouplingDtoForWriteDb::class)
interface ClassCouplingDtoDaoForInsert {
    @GetGeneratedKeys
    @SqlUpdate("insert into class_coupling (" +
            "class_id, project_id, inner_fan_in, inner_fan_out, outer_fan_in, " +
            "outer_fan_out, inner_instability, inner_coupling, outer_instability, outer_coupling) " +
            "values(:classId, :projectId, :innerFanIn, :innerFanOut, :outerFanIn, " +
            ":outerFanOut, :innerInstability, :innerCoupling, :outerInstability, :outerCoupling)")
    fun insert(@BindBean classCouplingDtoForWriteDb: ClassCouplingDtoForWriteDb): Long
}

@RegisterBeanMapper(ClassCouplingDtoForReadFromDb::class)
interface ClassCouplingDtoDaoForRead {
    @SqlQuery("select c.class_id, c.project_id, j.name, j.module, " +
            "c.inner_fan_in, c.inner_fan_out, c.outer_fan_in, c.outer_fan_out " +
            "from class_coupling c join JClass j where c.class_id = j.id and j.id = :classId")
    fun findClassCoupling(@Bind("classId") classId: String): ClassCouplingDtoForReadFromDb?

    @SqlQuery("select c.class_id, c.project_id, j.name, j.module, " +
            "c.inner_fan_in, c.inner_fan_out, c.outer_fan_in, c.outer_fan_out " +
            "from class_coupling c join JClass j where c.class_id = j.id and j.id in (<classIds>)")
    fun findClassCouplings(@BindList("classIds") classIds: List<String>): List<ClassCouplingDtoForReadFromDb>
}

class ClassCouplingDtoForReadFromDb(var classId: String,
                                    var projectId: Int,
                                    var name: String,
                                    var module: String,
                                    var innerFanIn: Int,
                                    var innerFanOut: Int,
                                    var outerFanIn: Int,
                                    var outerFanOut: Int) {
    constructor() : this("", 0, "", "", 0, 0, 0, 0)

    fun toClassCoupling(): ClassCoupling {
        val jClassVO = JClassVO(name, module)
        jClassVO.id = classId
        return ClassCoupling(jClassVO, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }
}


data class ClassCouplingDtoForWriteDb(val classId: String,
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
        fun fromClassCoupling(classCoupling: ClassCoupling): ClassCouplingDtoForWriteDb {
            return ClassCouplingDtoForWriteDb(classCoupling.jClassVO.id!!, 0, classCoupling.innerFanIn, classCoupling.innerFanOut,
                    classCoupling.outerFanIn, classCoupling.outerFanOut, classCoupling.innerInstability, classCoupling.innerCoupling,
                    classCoupling.outerInstability, classCoupling.outerCoupling)
        }
    }
}

