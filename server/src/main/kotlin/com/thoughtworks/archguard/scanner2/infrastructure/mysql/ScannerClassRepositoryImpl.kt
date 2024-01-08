package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import org.archguard.model.Dependency
import org.archguard.model.code.JClass
import org.archguard.model.code.JField
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class ScannerClassRepositoryImpl(val jdbi: Jdbi) : JClassRepository {

    override fun findClassParents(systemId: Long, module: String?, name: String?): List<JClass> {
        val moduleFilter = if (module == null) {
            "and c.module is NULL"
        } else {
            "and c.module='$module'"
        }
        val sql = "SELECT DISTINCT p.id as id, p.name as name, p.module as module, p.loc as loc, p.access as access " +
            "           FROM code_class c,`code_ref_class_parent` cp,code_class p" +
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
        val moduleFilter = if (module == null) {
            "and p.module is NULL"
        } else {
            "and p.module='$module'"
        }
        val sql = "SELECT DISTINCT c.id as id, c.name as name, c.module as module, c.loc as loc, c.access as access " +
            "           FROM code_class c,`code_ref_class_parent` cp,code_class p" +
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
        val sql = "SELECT id, name, type FROM code_field WHERE id in (select b from code_ref_class_fields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                .mapTo(JField::class.java)
                .list()
        }
    }

    override fun getDistinctClassDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>> {
        return getAllClassDependenciesAndNotThirdParty(systemId).toSet().toList()
    }

    override fun getAllClassDependenciesAndNotThirdParty(systemId: Long): List<Dependency<String>> {
        val sql = "select a as caller, b as callee from code_ref_class_dependencies  where system_id = :systemId " +
            "and a in (select id from code_class where code_class.system_id = :systemId and is_thirdparty = 0 and is_test = 0) " +
            "and b in (select id from code_class where code_class.system_id = :systemId and is_thirdparty = 0 and is_test = 0) " +
            "and a!=b"
        return jdbi.withHandle<List<IdDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(IdDependencyDto::class.java))
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(IdDependencyDto::class.java)
                .list()
        }.map { it.toDependency() }
    }

    override fun findClassBy(systemId: Long, name: String, module: String?): JClass? {
        val moduleFilter = if (module == null) {
            "and module is NULL"
        } else {
            "and module='$module'"
        }
        val sql = "SELECT id, name, module, loc, access FROM code_class where system_id = $systemId and name = '$name' $moduleFilter limit 1"
        val withHandle = jdbi.withHandle<JClassPO?, RuntimeException> {
            it.registerRowMapper(ConstructorMapper.factory(JClassPO::class.java))
            it.createQuery(sql)
                .mapTo(JClassPO::class.java)
                .firstOrNull()
        }
        return withHandle?.toJClass()
    }

    override fun getJClassesNotThirdPartyAndNotTest(systemId: Long): List<JClass> {
        val sql = "SELECT id, name, module, loc, access FROM code_class where system_id = :systemId " +
            "and is_thirdparty = 0 and is_test = 0"
        return jdbi.withHandle<List<JClassPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassPO::class.java))
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(JClassPO::class.java)
                .list()
        }.map { it.toJClass() }
    }
}
