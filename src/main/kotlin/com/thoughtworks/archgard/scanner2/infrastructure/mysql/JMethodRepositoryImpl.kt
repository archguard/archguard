package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.model.JField
import com.thoughtworks.archgard.scanner2.domain.model.JMethod
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class JMethodRepositoryImpl(val jdbi: Jdbi) : JMethodRepository {

    override fun findMethodFields(id: String): List<JField> {
        val sql = "SELECT id, name, type FROM JField WHERE id in (select b from _MethodFields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                    .mapTo(JField::class.java)
                    .list()
        }
    }

    override fun getAllMethodDependencies(systemId: Long): List<Dependency<String>> {
        val sql = "select DISTINCT a as caller, b as callee from _MethodCallees  where system_id = :systemId " +
                "and a in (select id from JMethod where JMethod.system_id = :systemId and module != 'null') " +
                "and b in (select id from JMethod where JMethod.system_id = :systemId and module != 'null') " +
                "and a!=b"
        return jdbi.withHandle<List<IdDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(IdDependencyDto::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(IdDependencyDto::class.java)
                    .list()
        }.map { it.toDependency() }
    }

    override fun findMethodsByModuleAndClass(systemId: Long, module: String, name: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE clzname='$name' AND module='$module' AND system_id='$systemId'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodPO::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun getMethodsHasModules(systemId: Long): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE " +
                "system_id=:systemId AND module!='null'"
        return jdbi.withHandle<List<JMethodPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodPO::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(JMethodPO::class.java)
                    .list()
        }.map { it.toJMethod() }.filter { it.module != "null" }
    }

    override fun findMethodCallees(id: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access  FROM JMethod WHERE id IN (SELECT b FROM _MethodCallees WHERE a='$id') "
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodPO::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }
}
