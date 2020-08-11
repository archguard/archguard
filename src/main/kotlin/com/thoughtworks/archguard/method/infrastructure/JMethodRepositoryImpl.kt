package com.thoughtworks.archguard.method.infrastructure

import com.thoughtworks.archguard.clazz.infrastructure.JClassRepositoryImpl
import com.thoughtworks.archguard.common.TypeMap
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
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access  FROM JMethod WHERE clzname='$name' AND module='$module'"
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

    override fun findMethodByModuleAndClazzAndName(moduleName: String, clazzName: String, methodName: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE " +
                "name='$methodName' AND clzname='$clazzName' AND module='$moduleName'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }

    override fun findMethodByClazzAndName(clazzName: String, methodName: String): List<JMethod> {
        val sql = "SELECT id, name, clzname as clazz, module, returntype, argumenttypes, access FROM JMethod WHERE " +
                "name='$methodName' AND clzname='$clazzName'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDto::class.java)
                    .map { it.toJMethod() }
                    .list()
        }
    }
}

class JMethodDto(val id: String, val name: String, val clazz: String, val module: String, val returnType: String, val argumentTypes: String?, val access: String) {
    fun toJMethod(): JMethod {
        val argumentTypeList = if (argumentTypes.isNullOrBlank()) emptyList() else argumentTypes.split(",")
        val jMethod = JMethod(id, name, clazz, module, returnType, argumentTypeList)
        TypeMap.getMethodType(access.toInt()).forEach { jMethod.addType(it) }
        return jMethod
    }

}
