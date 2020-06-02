package com.thoughtworks.archguard.dependence.domain.logic_module

interface LogicModuleRepository {
    fun getAll(): List<LogicModule>
    fun update(id: String, logicModule: LogicModule)
    fun create(logicModule: LogicModule)
    fun delete(id: String)
    fun getDependence(caller: String, callee: String): List<ModuleDependency>
    fun deleteAll()
    fun saveAll(logicModules: List<LogicModule>)
}
