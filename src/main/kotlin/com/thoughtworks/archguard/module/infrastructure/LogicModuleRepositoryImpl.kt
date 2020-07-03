package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.Dependency
import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.JMethod
import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.LogicModuleStatus
import com.thoughtworks.archguard.module.domain.ModuleMember
import com.thoughtworks.archguard.module.domain.ModuleMemberType
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
            it.createQuery("select id, name, members, status from logic_module")
                    .mapTo(LogicModuleDTO::class.java)
                    .list()
        }
        return modules.map {
            LogicModule(it.id, it.name, it.members.split(',').sorted()
                    .map { m -> ModuleMember.createModuleMember(m) }, it.status)
        }
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

    override fun updateAll(logicModules: List<LogicModule>) {
        logicModules.forEach {
            update(it.id.toString(), it)
        }
    }

    override fun getDependence(caller: String, callee: String): List<Dependency<JMethod>> {
        val callerTemplate = defineTableTemplate(getMembers(caller))
        val calleeTemplate = defineTableTemplate(getMembers(callee))

        val sql = "select a.module caller, a.clzname callerClass, a.name callerMethod, b.module callee, b.clzname calleeClass, b.name calleeMethod from ($callerTemplate) a, ($calleeTemplate) b, _MethodCallees mc where a.id = mc.a and b.id = mc.b"

        return jdbi.withHandle<List<ModuleDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ModuleDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(ModuleDependencyDto::class.java)
                    .list()
        }.map { it.toMethodDependency() }
    }

    override fun getAllClassDependency(members: List<ModuleMember>): List<Dependency<JClass>> {
        val tableTemplate = defineTableTemplateNew(members)

        val sql = "select a.module as moduleCaller, a.clzname as classCaller, " +
                "b.module as moduleCallee, b.clzname as classCallee " +
                "from ($tableTemplate) a, ($tableTemplate) b,  _MethodCallees mc " +
                "where a.id = mc.a and b.id = mc.b"
        return jdbi.withHandle<List<Dependency<JClass>>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDependencyDto::class.java)
                    .list()
                    .map { jClassDependencyDto -> jClassDependencyDto.toJClassDependency() }
                    .filter { dependency -> dependency.caller != dependency.callee }
        }
    }

    fun getMembers(name: String): List<String> {
        val sql = "select members from logic_module where name = '$name'"
        val members = jdbi.withHandle<String, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .first()
        }

        return members.split(',')
    }

    override fun getParentClassId(id: String): List<String> {
        val sql = "select b from _ClassParent where a = '$id'"
        return jdbi.withHandle<List<String>, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    fun defineTableTemplate(members: List<String>): String {
        var tableTemplate = "select * from JMethod where ("
        val filterConditions = ArrayList<String>()
        members.forEach { s ->
            val member = s.split('.')
            if (member.size == 1) {
                filterConditions.add("module = '${member[0]}'")
            }
            if (member.size > 1) {
                filterConditions.add("(module = '${member[0]}' and clzname like '${member.subList(1, member.size).joinToString(".") + "."}%')")
                filterConditions.add("(module = '${member[0]}' and clzname='${member.subList(1, member.size).joinToString(".")}')")
            }
        }
        tableTemplate += filterConditions.joinToString(" or ")
        tableTemplate += ")"
        println(tableTemplate)
        return tableTemplate
    }

    private fun defineTableTemplateNew(members: List<ModuleMember>): String {
        var tableTemplate = "select * from JMethod where ("
        val filterConditions = ArrayList<String>()
        members.forEach { s ->
            if (s.getType() == ModuleMemberType.SUBMODULE) {
                filterConditions.add("module = '${s.getFullName()}'")
            }
            if (s.getType() == ModuleMemberType.CLASS) {
                val jclass = s as JClass
                filterConditions.add("(module = '${jclass.module}' and clzname like '${jclass.name + "."}%')")
                filterConditions.add("(module = '${jclass.module}' and clzname='${jclass.name}')")
            }
        }
        tableTemplate += filterConditions.joinToString(" or ")
        tableTemplate += ")"
        println(tableTemplate)
        return tableTemplate
    }

}

class JClassDependencyDto(val moduleCaller: String, val classCaller: String, val moduleCallee: String, val classCallee: String) {
    fun toJClassDependency(): Dependency<JClass> {
        return Dependency(JClass(classCaller, moduleCaller), JClass(classCallee, moduleCallee))
    }
}

