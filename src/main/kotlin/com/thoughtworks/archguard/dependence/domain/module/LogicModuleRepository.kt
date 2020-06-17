package com.thoughtworks.archguard.dependence.domain.module

interface LogicModuleRepository {
    fun getAllNormal(): List<LogicModule>
    fun getAll(): List<LogicModule>
    fun update(id: String, logicModule: LogicModule)
    fun create(logicModule: LogicModule)
    fun delete(id: String)
    fun getDependence(caller: String, callee: String): List<ModuleDependency>
    fun deleteAll()
    fun saveAll(logicModules: List<LogicModule>)
    fun getAllCallerCalleeCoupleAtClassLevel(members: List<String>): List<CallerCalleeCouple>
    fun getParentClassId(id: String): List<String>
}
