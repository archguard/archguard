package com.thoughtworks.archgard.scanner2.infrastructure.persist

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.model.JField
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.infrastructure.TypeMap
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl(val jdbi: Jdbi) : JClassRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    override fun findClassParents(systemId: Long, module: String?, name: String?): List<JClass> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and c.module='$module'"
        }
        val sql = "SELECT DISTINCT p.id as id, p.name as name, p.module as module, p.loc as loc, p.access as access " +
                "           FROM JClass c,`_ClassParent` cp,JClass p" +
                "           WHERE c.id = cp.a AND cp.b = p.id" +
                "           And c.system_id = $systemId" +
                "           AND c.name = '$name' $moduleFilter"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findClassImplements(systemId: Long, name: String?, module: String?): List<JClass> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and p.module='$module'"
        }
        val sql = "SELECT DISTINCT c.id as id, c.name as name, c.module as module, c.loc as loc, c.access as access " +
                "           FROM JClass c,`_ClassParent` cp,JClass p" +
                "           WHERE c.id = cp.a AND cp.b = p.id" +
                "           AND c.system_id = $systemId" +
                "           AND p.name = '$name' $moduleFilter"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findFields(id: String): List<JField> {
        val sql = "SELECT id, name, type FROM JField WHERE id in (select b from _ClassFields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                    .mapTo(JField::class.java)
                    .list()
        }
    }

    fun getJClassBy(systemId: Long, name: String, module: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where system_id=:systemId and name=:name and module=:module"
        val jClassDto: JClassDto? = jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("name", name)
                    .bind("module", module)
                    .mapTo(JClassDto::class.java)
                    .findOne().orElse(null)
        }
        return jClassDto?.toJClass()
    }

    fun getAllBysystemId(systemId: Long): List<JClass> {
        val sql = "SELECT id, name, module, loc, access FROM JClass where system_id = :systemId"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getJClassesHasModules(systemId: Long): List<JClass> {
        return this.getAllBysystemId(systemId).filter { it.module != "null" }
    }

}
class JClassDto(val id: String, val name: String, val module: String, val loc: Int?, val access: String?) {
    fun toJClass(): JClass {
        val jClass = JClass(id, name, module)
        if (access == null) {
            return jClass
        }
        val accessInt = access.toIntOrNull()
        if (accessInt != null) {
            TypeMap.getClassType(accessInt).forEach { jClass.addClassType(it) }
        }
        return jClass
    }
}
