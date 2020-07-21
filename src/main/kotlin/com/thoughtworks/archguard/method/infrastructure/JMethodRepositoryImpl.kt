package com.thoughtworks.archguard.method.infrastructure

import com.thoughtworks.archguard.clazz.infrastructure.JClassRepositoryImpl
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JMethodRepositoryImpl : JMethodRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    @Autowired
    lateinit var jdbi: Jdbi
    override fun findMethodsByModuleAndClass(module: String, name: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module FROM JMethod WHERE clzname='$name' AND module='$module'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethod::class.java)
                    .list()
        }
    }

    override fun findMethodCallees(id: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module FROM JMethod WHERE id IN (SELECT b FROM _MethodCallees WHERE a='id') "
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethod::class.java)
                    .list()
        }
    }

    override fun findMethodImplements(id: String, name: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module " +
                "FROM JMethod " +
                "WHERE id IN (SELECT DISTINCT cm.b " +
                "             FROM JClass c, " +
                "                  _ClassMethods cm, " +
                "                  JClass p, " +
                "                  _ClassMethods pm, " +
                "                  _ClassParent cp " +
                "             WHERE pm.b = '$id' " +
                "               AND p.id = pm.a " +
                "               AND cp.b = p.id " +
                "               AND c.id = cp.a " +
                "               AND cm.a = c.id) " +
                "  AND name = '$name'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethod::class.java)
                    .list()
        }
    }
}