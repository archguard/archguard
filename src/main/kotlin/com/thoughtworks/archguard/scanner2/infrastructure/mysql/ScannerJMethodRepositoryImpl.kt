package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.Dependency
import com.thoughtworks.archguard.scanner2.domain.model.JField
import com.thoughtworks.archguard.scanner2.domain.model.JMethod
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class ScannerJMethodRepositoryImpl(val jdbi: Jdbi) : JMethodRepository {

    override fun findMethodFields(id: String): List<JField> {
        val sql = "SELECT id, name, type FROM code_field WHERE id in (select b from code_ref_method_fields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                    .mapTo(JField::class.java)
                    .list()
        }
    }

    override fun getDistinctMethodDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>> {
        return getAllMethodDependenciesAndNotThirdParty(systemId).toSet().toList()
    }

    override fun getAllMethodDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>> {
        val sql = "select a as caller, b as callee from code_ref_method_callees where system_id = :systemId " +
                "and a in (select id from code_method where code_method.system_id = :systemId and is_test = 0 and module is not NULL) " +
                "and b in (select id from code_method where code_method.system_id = :systemId and is_test = 0 and module is not NULL) " +
                "and a!=b"
        return jdbi.withHandle<List<IdDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(IdDependencyDto::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(IdDependencyDto::class.java)
                    .list()
        }.map { it.toDependency() }
    }

    override fun findMethodsByModuleAndClass(systemId: Long, module: String?, name: String): List<JMethod> {
        val moduleFilter = if (module == null) {
            "and module is NULL"
        } else {
            "and module='$module'"
        }
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE clzname='$name' AND system_id='$systemId' " + moduleFilter
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodPO::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun getMethodsNotThirdParty(systemId: Long): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE " +
                "system_id=:systemId AND module is not NULL"
        return jdbi.withHandle<List<JMethodPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodPO::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(JMethodPO::class.java)
                    .list()
        }.map { it.toJMethod() }
    }

    override fun getMethodsNotThirdPartyAndNotTest(systemId: Long): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE " +
                "system_id=:systemId and is_test = 0 AND module is not NULL and name not like '<%>'"
        return jdbi.withHandle<List<JMethodPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodPO::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(JMethodPO::class.java)
                    .list()
        }.map { it.toJMethod() }
    }

    override fun findMethodCallees(id: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE id IN (SELECT b FROM code_ref_method_callees WHERE a='$id') "
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodPO::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }
}
