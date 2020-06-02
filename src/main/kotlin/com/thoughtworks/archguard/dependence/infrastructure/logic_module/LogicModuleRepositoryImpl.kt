package com.thoughtworks.archguard.dependence.infrastructure.logic_module

import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModule
import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModuleRepository
import com.thoughtworks.archguard.dependence.domain.logic_module.ModuleDependency
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LogicModuleRepositoryImpl : LogicModuleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAll(): List<LogicModule> {
        var modules = jdbi.withHandle<List<LogicModuleDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(LogicModuleDTO::class.java))
            it.createQuery("select id, name, members from logic_module")
                    .mapTo(LogicModuleDTO::class.java)
                    .list()
        }
        return modules.map { LogicModule(it.id, it.name, it.members.split(',')) }
    }

    override fun update(id: String, logicModule: LogicModule) {
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("update logic_module set " +
                    "name = '${logicModule.name}', " +
                    "members = '${logicModule.members.joinToString(",")}'" +
                    "where id = '${logicModule.id}'")
                    .execute()
        }
    }

    override fun create(logicModule: LogicModule) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("insert into logic_module (id, name, members) values (?, ?, ?)", logicModule.id, logicModule.name, logicModule.members.joinToString(","));
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
                handle.execute("insert into logic_module (id, name, members) values (?, ?, ?)", it.id, it.name, it.members.joinToString(","));
            }
        }
    }


    override fun getDependence(caller: String, callee: String): List<ModuleDependency> {
        var callerTemplate = defineTableTemplate(getMembers(caller))
        var calleeTemplate = defineTableTemplate(getMembers(callee))

        var sql = "select a.clzname callerClass, a.name callerMethod, b.clzname calleeClass, b.name calleeMethod from ($callerTemplate) a, ($callerTemplate) b, _MethodCallees mc where a.id = mc.a and b.id = mc.b"

        println(sql)
        return jdbi.withHandle<List<ModuleDependency>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ModuleDependency::class.java))
            it.createQuery(sql)
                    .mapTo(ModuleDependency::class.java)
                    .list()
        }

    }

    fun getMembers(name: String): List<String> {
        var sql = "select members from logic_module where name = '$name'"
        var members = jdbi.withHandle<String, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .first();
        }

        return members.split(',')
    }

    fun defineTableTemplate(members: List<String>): String {
        var tableTemplate = "select * from JMethod where ("
        var filterConditions = ArrayList<String>()
        members.forEach { s ->
            var member = s.split('.')
            if (member.size == 1) {
                filterConditions.add("module = '${member[0]}'")
            }
            if (member.size > 1) {
                filterConditions.add("module = '${member[0]}' and clzname like '${member.subList(1, member.size).joinToString(".")}%")
            }
        }
        tableTemplate += filterConditions.joinToString(" or ")
        tableTemplate += ")"
        return tableTemplate
    }

}

