package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.*
import com.thoughtworks.archguard.module.infrastructure.dto.MethodDependencyDto
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LogicModuleRepositoryImpl : LogicModuleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllByShowStatus(isShow: Boolean): List<LogicModule> {
        if (isShow) {
            return this.getAll().filter { it.status == LogicModuleStatus.NORMAL }
        }
        return this.getAll().filter { it.status == LogicModuleStatus.HIDE }
    }

    override fun getAll(): List<LogicModule> {
        val modules = jdbi.withHandle<List<LogicModuleDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(LogicModuleDTO::class.java))
            it.createQuery("select id, name, members, lg_members, status from logic_module")
                    .mapTo(LogicModuleDTO::class.java)
                    .list()
        }
        return modules.map { it.toLogicModule(this) }
    }

    override fun update(id: String, logicModule: LogicModule) {
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("update logic_module set " +
                    "name = '${logicModule.name}', " +
                    "members = '${logicModule.members.map { moduleMember -> moduleMember.getFullName() }.joinToString(",")}', " +
                    "status = '${logicModule.status}' " +
                    "where id = '${logicModule.id}'")
                    .execute()
        }
    }

    override fun get(name: String): LogicModule {
        return jdbi.withHandle<LogicModuleDTO, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(LogicModuleDTO::class.java))
            it.createQuery("select id, name, members, lg_members, status from logic_module where name='$name'")
                    .mapTo(LogicModuleDTO::class.java)
                    .one()
        }.toLogicModule(this)
    }

    override fun create(logicModule: LogicModule) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("insert into logic_module (id, name, members, status) values (?, ?, ?, ?)",
                    logicModule.id, logicModule.name, logicModule.members.joinToString(",") { it.getFullName() }, logicModule.status)
        }
    }

    override fun delete(id: String) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("delete from logic_module where id = '$id'")
        }
    }

    override fun deleteAll() {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("delete from logic_module")
        }
    }

    override fun saveAll(logicModules: List<LogicModule>) {
        logicModules.forEach {
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.execute("insert into logic_module (id, name, members, status) values (?, ?, ?, ?)",
                        it.id, it.name, it.members.joinToString(",") { moduleMember -> moduleMember.getFullName() }, it.status)
            }
        }
    }

    override fun getAllSubModule(): List<SubModule> {
        val subModulesFromJClasses = jdbi.withHandle<List<SubModule>, Nothing> {
            it.createQuery("select distinct module from JClass")
                    .mapTo(String::class.java)
                    .list()
                    .filter { it -> it != "null" }
                    .map { it -> SubModule(it) }
        }
        val subModulesFromJMethods = jdbi.withHandle<List<SubModule>, Nothing> {
            it.createQuery("select distinct module from JMethod")
                    .mapTo(String::class.java)
                    .list()
                    .filter { it -> it != "null" }
                    .map { it -> SubModule(it) }
        }
        return subModulesFromJClasses.union(subModulesFromJMethods).toList()
    }

    override fun updateAll(logicModules: List<LogicModule>) {
        logicModules.forEach {
            update(it.id.toString(), it)
        }
    }

    override fun getDependence(caller: String, callee: String): List<Dependency<JMethod>> {
        val callerTemplate = generateTableSqlTemplateWithModuleModules(this.get(caller).members)
        val calleeTemplate = generateTableSqlTemplateWithModuleModules(this.get(callee).members)

        val sql = "select a.module caller, a.clzname callerClass, a.name callerMethod, b.module callee, b.clzname calleeClass, b.name calleeMethod from ($callerTemplate) a, ($calleeTemplate) b, _MethodCallees mc where a.id = mc.a and b.id = mc.b"

        return jdbi.withHandle<List<MethodDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(MethodDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(MethodDependencyDto::class.java)
                    .list()
        }.map { it.toMethodDependency() }
    }

    override fun getParentClassId(id: String): List<String> {
        val sql = "select b from _ClassParent where a = '$id'"
        return jdbi.withHandle<List<String>, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .list()
        }
    }

}

fun generateTableSqlTemplateWithModuleModules(members: List<LogicComponent>): String {
    var tableTemplate = "select * from JMethod where ("
    val filterConditions = ArrayList<String>()
    members.forEach { s ->
        if (s.getType() == ModuleMemberType.SUBMODULE) {
            filterConditions.add("module = '${s.getFullName()}'")
        }
        if (s.getType() == ModuleMemberType.CLASS) {
            val jclass = s as JClassVO
            filterConditions.add("(module = '${jclass.module}' and clzname like '${jclass.name + "."}%')")
            filterConditions.add("(module = '${jclass.module}' and clzname='${jclass.name}')")
        }
    }
    tableTemplate += filterConditions.joinToString(" or ")
    tableTemplate += ")"
    println(tableTemplate)
    return tableTemplate
}

class LogicModuleDTO(val id: String, val name: String, val members: String?, private val lgMembers: String?, private val status: LogicModuleStatus) {
    fun toLogicModule(logicModuleRepository: LogicModuleRepository): LogicModule {
        val leafMembers = members?.split(',')?.sorted()?.map { m -> LogicComponent.createLeaf(m) } ?: emptyList()
        val lgMembers = lgMembers?.split(',')?.sorted()?.map { m -> logicModuleRepository.get(m) } ?: emptyList()
        val logicModule = LogicModule.create(id, name, leafMembers, lgMembers)
        logicModule.status = status
        return logicModule
    }
}
