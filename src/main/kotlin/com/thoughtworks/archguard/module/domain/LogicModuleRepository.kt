package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule

interface LogicModuleRepository {
    fun getAllByShowStatus(isShow: Boolean): List<LogicModule>
    fun getAll(): List<LogicModule>
    fun get(name: String): LogicModule
    fun update(id: String, logicModule: LogicModule)
    fun updateAll(logicModules: List<LogicModule>)
    fun create(logicModule: LogicModule)
    fun createWithCompositeNodes(logicModule: LogicModuleWithCompositeNodes)
    fun delete(id: String)
    fun deleteAll()
    fun saveAll(logicModules: List<LogicModule>)
    fun getAllSubModule(projectId: Long): List<SubModule>
    fun getParentClassId(id: String): List<String>
}
