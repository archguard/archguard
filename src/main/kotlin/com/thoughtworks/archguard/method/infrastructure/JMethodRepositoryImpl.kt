package com.thoughtworks.archguard.method.infrastructure

import com.thoughtworks.archguard.clazz.domain.JField
import com.thoughtworks.archguard.clazz.infrastructure.JClassRepositoryImpl
import com.thoughtworks.archguard.common.TypeMap
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class JMethodRepositoryImpl(val jdbi: Jdbi) : JMethodRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    override fun findMethodFields(id: String): List<JField> {
        val sql = "SELECT id, name, type FROM JField WHERE id in (select b from _MethodFields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                    .mapTo(JField::class.java)
                    .list()
        }
    }

    override fun findMethodsByModuleAndClass(systemId: Long, module: String?, name: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE clzname='$name' AND system_id='$systemId' AND module <=>'$module'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun findMethodCallers(id: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access  FROM JMethod WHERE id IN (SELECT a FROM _MethodCallees WHERE b='$id') "
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun findMethodCallees(id: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access  FROM JMethod WHERE id IN (SELECT b FROM _MethodCallees WHERE a='$id') "
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun findMethodImplements(id: String, name: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access  " +
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
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun findMethodByModuleAndClazzAndName(systemId: Long, moduleName: String?, clazzName: String, methodName: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE " +
                "system_id=:systemId AND name=:methodName AND clzname=:clazzName AND module <=>'$moduleName'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("methodName", methodName)
                    .bind("clazzName", clazzName)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }
}

class JMethodDto(val id: String, val name: String, val clazz: String, val module: String?, val returnType: String, val argumentTypes: String?, val access: String) {
    fun toJMethod(): JMethod {
        val argumentTypeList = if (argumentTypes.isNullOrBlank()) emptyList() else argumentTypes.split(",")
        val jMethod = JMethod(id, name, clazz, module, returnType, argumentTypeList)
        TypeMap.getMethodType(access.toInt()).forEach { jMethod.addType(it) }
        return jMethod
    }

}
