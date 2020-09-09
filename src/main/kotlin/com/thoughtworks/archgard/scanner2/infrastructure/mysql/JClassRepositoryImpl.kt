package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.model.JField
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl(val jdbi: Jdbi) : JClassRepository {

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
        return jdbi.withHandle<List<JClassPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassPO::class.java))
            it.createQuery(sql)
                    .mapTo(JClassPO::class.java)
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
        return jdbi.withHandle<List<JClassPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassPO::class.java))
            it.createQuery(sql)
                    .mapTo(JClassPO::class.java)
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

    override fun getJClassesHasModules(systemId: Long): List<JClass> {
        val sql = "SELECT id, name, module, loc, access FROM JClass where system_id = :systemId"
        return jdbi.withHandle<List<JClassPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassPO::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(JClassPO::class.java)
                    .list()
        }.map { it.toJClass() }.filter { it.module != "null" }
    }

}

