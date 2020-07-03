package com.thoughtworks.archguard.module.domain

interface LogicModuleRepository {
    fun getAllByShowStatus(isShow: Boolean): List<LogicModuleLegacy>
    fun getAllByShowStatusNew(isShow: Boolean): List<LogicModule>
    fun getAll(): List<LogicModuleLegacy>
    fun getAllNew(): List<LogicModule>
    fun update(id: String, logicModule: LogicModuleLegacy)
    fun updateAll(logicModules: List<LogicModuleLegacy>)
    fun create(logicModule: LogicModuleLegacy)
    fun delete(id: String)
    fun getDependence(caller: String, callee: String): List<ModuleDependency>
    fun deleteAll()
    fun saveAll(logicModules: List<LogicModule>)
    fun getAllClassDependency(members: List<String>): List<DependencyLegacy>
    fun getAllClassDependencyNew(members: List<ModuleMember>): List<Dependency<JClass>>
    fun getParentClassId(id: String): List<String>
}
