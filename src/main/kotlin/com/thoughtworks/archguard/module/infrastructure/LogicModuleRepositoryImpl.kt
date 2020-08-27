package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.LogicModuleWithCompositeNodes
import com.thoughtworks.archguard.module.domain.model.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LogicModuleRepositoryImpl : LogicModuleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllByShowStatus(projectId: Long, isShow: Boolean): List<LogicModule> {
        if (isShow) {
            return this.getAllByProjectId(projectId).filter { it.status == LogicModuleStatus.NORMAL }
        }
        return this.getAllByProjectId(projectId).filter { it.status == LogicModuleStatus.HIDE }
    }

    override fun getAllByProjectId(projectId: Long): List<LogicModule> {
        val modules = jdbi.withHandle<List<LogicModuleDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(LogicModuleDTO::class.java))
            it.createQuery("select id, name, members, lg_members, status from logic_module where project_id = :projectId")
                    .bind("projectId", projectId)
                    .mapTo(LogicModuleDTO::class.java)
                    .list()
        }
        return modules.map { it.toLogicModule(projectId, this) }
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

    override fun get(projectId: Long, name: String): LogicModule {
        return jdbi.withHandle<LogicModuleDTO, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(LogicModuleDTO::class.java))
            it.createQuery("select id, name, members, lg_members, status from logic_module where name=:name and project_id=:projectId")
                    .bind("projectId", projectId)
                    .bind("name", name)
                    .mapTo(LogicModuleDTO::class.java)
                    .one()
        }.toLogicModule(projectId, this)
    }

    // FIXME: add projectId
    override fun create(projectId: Long, logicModule: LogicModule) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("insert into logic_module (id, project_id, name, members, status) values (?, ?, ?, ?, ?)",
                    logicModule.id, projectId, logicModule.name, logicModule.members.joinToString(",") { it.getFullName() }, logicModule.status)
        }
    }

    // FIXME: add projectId
    override fun createWithCompositeNodes(projectId: Long, logicModule: LogicModuleWithCompositeNodes) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("insert into logic_module (id, project_id, name, lg_members, status) values (?, ?, ?, ?, ?)",
                    logicModule.id, projectId, logicModule.name, logicModule.lgMembers.joinToString(","), logicModule.status)
        }
    }

    override fun delete(id: String) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("delete from logic_module where id = '$id'")
        }
    }

    override fun deleteByProjectId(projectId: Long) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("delete from logic_module where project_id = ?", projectId)
        }
    }

    override fun saveAll(projectId: Long, logicModules: List<LogicModule>) {
        logicModules.forEach {
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.execute("insert into logic_module (id, project_id, name, members, status) values (?, ?, ?, ?, ?)",
                        it.id, projectId, it.name, it.members.joinToString(",") { moduleMember -> moduleMember.getFullName() }, it.status)
            }
        }
    }

    override fun getAllSubModule(projectId: Long): List<SubModule> {
        val subModulesFromJClasses = jdbi.withHandle<List<SubModule>, Nothing> { handle ->
            handle.createQuery("select distinct module from JClass where project_id = :projectId")
                    .bind("projectId", projectId)
                    .mapTo(String::class.java)
                    .list()
                    .filter { it != "null" }
                    .map { SubModule(it) }
        }
        val subModulesFromJMethods = jdbi.withHandle<List<SubModule>, Nothing> { handle ->
            handle.createQuery("select distinct module from JMethod where project_id = :projectId")
                    .bind("projectId", projectId)
                    .mapTo(String::class.java)
                    .list()
                    .filter { it != "null" }
                    .map { SubModule(it) }
        }
        return subModulesFromJClasses.union(subModulesFromJMethods).toList()
    }

    override fun updateAll(logicModules: List<LogicModule>) {
        logicModules.forEach {
            update(it.id, it)
        }
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
    return tableTemplate
}

class LogicModuleDTO(val id: String, val name: String, val members: String?, private val lgMembers: String?, private val status: LogicModuleStatus) {
    fun toLogicModule(projectId: Long, logicModuleRepository: LogicModuleRepository): LogicModule {
        val leafMembers = members?.split(',')?.sorted()?.map { m -> LogicComponent.createLeaf(m) } ?: emptyList()
        val lgMembers = lgMembers?.split(',')?.sorted()?.map { m -> logicModuleRepository.get(projectId, m) }
                ?: emptyList()
        val logicModule = LogicModule.create(id, name, leafMembers, lgMembers)
        logicModule.status = status
        return logicModule
    }
}
