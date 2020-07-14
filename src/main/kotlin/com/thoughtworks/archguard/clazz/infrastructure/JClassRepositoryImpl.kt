package com.thoughtworks.archguard.clazz.infrastructure

import com.thoughtworks.archguard.clazz.domain.PropsDependency
import com.thoughtworks.archguard.clazz.domain.FullName
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.domain.PropsDependencyDTO
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.infrastructure.dto.JClassDependencyDto
import com.thoughtworks.archguard.module.infrastructure.dto.JClassDto
import com.thoughtworks.archguard.module.infrastructure.generateTableSqlTemplateWithModuleModules
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl : JClassRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJClassBy(name: String, module: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where name='$name' and module='$module'"
        val jClassDto: JClassDto? = jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .findOne().orElse(null)
        }
        return jClassDto?.toJClass()
    }

    override fun getJClassByName(name: String): List<JClass> {
        val sql = "select id, name, module, loc, access from JClass where name='$name'"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findClassParents(name: String?, module: String?): List<PropsDependency<String>> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and p.module='$module'"
        }
        val sql = "SELECT DISTINCT c.name from JClass c,`_ClassParent` cp,JClass p" +
                "           WHERE c.id = cp.a AND cp.b = p.id" +
                "           AND p.name = $name $moduleFilter"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { PropsDependency(name ?: "", it.name, 1, mapOf(Pair("implements", true))) }
    }

    override fun findClassImplements(name: String?, module: String?): List<PropsDependency<String>> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and c.module='$module'"
        }
        val sql = "SELECT DISTINCT p.name from JClass c,`_ClassParent` cp,JClass p" +
                "           WHERE c.id = cp.a AND cp.b = p.id" +
                "           AND c.name = $name $moduleFilter"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { PropsDependency(it.name, name ?: "", 1, mapOf(Pair("parent", true))) }
    }

    override fun findCallees(name: String?, module: String?): List<PropsDependency<String>> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and a.module='$module'"
        }
        val sql = "SELECT b.clzname, COUNT(1) from JMethod a,`_MethodCallees` cl,JMethod b" +
                "                     WHERE a.id = cl.a and b.id = cl.b" +
                "                     AND a.clzname = $name $moduleFilter" +
                "                     AND b.clzname <> $name" +
                "                     GROUP BY b.clzname"
        return jdbi.withHandle<List<PropsDependencyDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(PropsDependencyDTO::class.java))
            it.createQuery(sql)
                    .mapTo(PropsDependencyDTO::class.java)
                    .list()
        }.map { it.toPropsDependency(mapOf()) }
    }

    override fun findCallers(name: String?, module: String?): List<PropsDependency<String>> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and a.module='$module'"
        }
        val sql = "SELECT a.clzname, COUNT(1) from JMethod a,`_MethodCallees` cl,JMethod b" +
                "                     WHERE a.id = cl.a and b.id = cl.b" +
                "                     AND a.clzname = $name $moduleFilter" +
                "                     AND b.clzname <> $name" +
                "                     GROUP BY a.clzname"
        return jdbi.withHandle<List<PropsDependencyDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(PropsDependencyDTO::class.java))
            it.createQuery(sql)
                    .mapTo(PropsDependencyDTO::class.java)
                    .list()
        }.map { it.toPropsDependency(mapOf()) }
    }

    override fun findDependencers(id: String?): List<JClass> {
        val sql = "select id, name, module, loc, access from JClass where id in (select a from _ClassDependences where b='${id}') and module != 'null'"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findDependencees(id: String?): List<JClass> {
        val sql = "select id, name, module, loc, access from JClass where id in (select b from _ClassDependences where a='${id}') and module != 'null'"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getJClassById(id: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where id='$id'"
        return jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .one()
        }.toJClass()
    }

    override fun getAll(): List<JClass> {
        val sql = "SELECT id, name, module, loc, access FROM JClass"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getAll(fullNames: List<FullName>): List<JClass> {
        val sql = "SELECT id, name, module, loc, access FROM JClass WHERE concat(module, '.', name) IN (<fullNameList>)"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .bindList("fullNameList", fullNames.map { fullName -> "${fullName.module}.${fullName.name}" })
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getJClassesHasModules(): List<JClass> {
        return this.getAll().filter { it.module != "null" }
    }

    override fun getAllClassDependency(members: List<LogicComponent>): List<Dependency<JClass>> {
        val tableTemplate = generateTableSqlTemplateWithModuleModules(members)

        val sql = "select a.module as moduleCaller, a.clzname as classCaller, " +
                "b.module as moduleCallee, b.clzname as classCallee " +
                "from ($tableTemplate) a, ($tableTemplate) b,  _MethodCallees mc " +
                "where a.id = mc.a and b.id = mc.b"
        val jClassDependencies = jdbi.withHandle<List<Dependency<JClass>>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDependencyDto::class.java)
                    .list()
                    .map { jClassDependencyDto -> jClassDependencyDto.toJClassDependency() }
                    .filter { dependency -> dependency.caller != dependency.callee }
        }

        val fullNames: List<FullName> = jClassDependencies
                .map { listOf(FullName(it.caller.name, it.caller.module), FullName(it.callee.name, it.callee.module)) }
                .flatten().toSet().toList()
        val jClassesRelated = getAll(fullNames)

        return jClassDependencies
                .map {
                    Dependency(updateJClassFields(it.caller, jClassesRelated) ?: it.caller,
                            updateJClassFields(it.callee, jClassesRelated) ?: it.callee)
                }
    }

    private fun updateJClassFields(jClass: JClass, jClasses: List<JClass>): JClass? {
        val matchedJClass = jClasses.filter { it -> it.name == jClass.name && it.module == jClass.module }
        if (matchedJClass.isEmpty()) {
            return null
        }
        if (matchedJClass.size > 1) {
            log.error("updateJClassFields matched more than one Class!")
        }
        return matchedJClass[0]
    }

    override fun getMethodsById(id: String): List<String> {
        val sql = "select b from _ClassMethods where a='$id'"
        return jdbi.withHandle<List<String>, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .list()
        }
    }
}
