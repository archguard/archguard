package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.JMethod
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.ModuleMember

interface LogicModuleRepository {
    fun getAllByShowStatus(isShow: Boolean): List<LogicModule>
    fun getAll(): List<LogicModule>
    fun get(name: String): LogicModule
    fun update(id: String, logicModule: LogicModule)
    fun updateAll(logicModules: List<LogicModule>)
    fun create(logicModule: LogicModule)
    fun delete(id: String)
    fun getDependence(caller: String, callee: String): List<Dependency<JMethod>>
    fun deleteAll()
    fun saveAll(logicModules: List<LogicModule>)

    fun getAllClassDependency(members: List<ModuleMember>): List<Dependency<JClass>>
    fun getParentClassId(id: String): List<String>
}
