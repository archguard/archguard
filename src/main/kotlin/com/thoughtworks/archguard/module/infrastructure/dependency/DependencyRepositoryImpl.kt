package com.thoughtworks.archguard.module.infrastructure.dependency

import com.thoughtworks.archguard.clazz.domain.FullName
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.infrastructure.dto.JClassDependencyDto
import com.thoughtworks.archguard.module.infrastructure.dto.JMethodDependencyDto
import com.thoughtworks.archguard.module.infrastructure.generateTableSqlTemplateWithModuleModules
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class DependencyRepositoryImpl : DependencyRepository {

    @Autowired
    lateinit var jdbi: Jdbi


    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun getAllMethodDependencies(): List<Dependency<JMethodVO>> {
        val sql = "select ${generateSelectMethodTemplate("a", "caller")}, ${generateSelectMethodTemplate("b", "callee")} " +
                "from JMethod a, JMethod b, `_MethodCallees` mc where a.module != 'null' and b.module != 'null' and a.id = mc.a and b.id = mc.b"

        return jdbi.withHandle<List<JMethodDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDependencyDto::class.java)
                    .list()
        }.map { it.toJMethodDependency() }
    }

    private fun generateSelectMethodTemplate(tableName: String, prefix: String): String{
        return "$tableName.module ${prefix}Module, $tableName.clzName ${prefix}Class, $tableName.name ${prefix}Method"
    }

    override fun getAllClassDependencyLegacy(members: List<LogicComponent>): List<Dependency<JClassVO>> {
        val tableTemplate = generateTableSqlTemplateWithModuleModules(members)

        val sql = "select a.module as moduleCaller, a.clzname as classCaller, " +
                "b.module as moduleCallee, b.clzname as classCallee " +
                "from ($tableTemplate) a, ($tableTemplate) b,  _MethodCallees mc " +
                "where a.id = mc.a and b.id = mc.b"
        val jClassDependencies = jdbi.withHandle<List<Dependency<JClassVO>>, Nothing> {
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
        val jClassesRelated = jClassRepository.getAll(fullNames).map { JClassVO(it.name, it.module, it.classType) }

        return jClassDependencies
                .map {
                    Dependency(updateJClassFields(it.caller, jClassesRelated)
                            ?: JClassVO(it.caller.name, it.caller.module),
                            updateJClassFields(it.callee, jClassesRelated)
                                    ?: JClassVO(it.callee.name, it.callee.module))
                }
    }

    private fun updateJClassFields(jClass: JClassVO, jClasses: List<JClassVO>): JClassVO? {
        val matchedJClass = jClasses.filter { it.name == jClass.name && it.module == jClass.module }
        if (matchedJClass.isEmpty()) {
            return null
        }
        return matchedJClass[0]
    }

}
