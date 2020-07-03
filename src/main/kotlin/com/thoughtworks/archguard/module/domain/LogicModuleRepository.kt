package com.thoughtworks.archguard.module.domain

interface LogicModuleRepository {
    fun getAllByShowStatus(isShow: Boolean): List<LogicModule>
    fun getAllByShowStatusNew(isShow: Boolean): List<NewLogicModule>
    fun getAll(): List<LogicModule>
    fun getAllNew(): List<NewLogicModule>
    fun update(id: String, logicModule: LogicModule)
    fun updateAll(logicModules: List<LogicModule>)
    fun create(logicModule: LogicModule)
    fun delete(id: String)
    fun getDependence(caller: String, callee: String): List<ModuleDependency>
    fun deleteAll()
    fun saveAll(logicModules: List<NewLogicModule>)
    fun getAllClassDependency(members: List<String>): List<Dependency>
    fun getAllClassDependencyNew(members: List<ModuleMember>): List<NewDependency<JClass>>
    fun getParentClassId(id: String): List<String>
}
