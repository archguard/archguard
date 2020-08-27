package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule

interface LogicModuleRepository {
    fun getAllByShowStatus(projectId: Long, isShow: Boolean): List<LogicModule>
    fun getAllByProjectId(projectId: Long): List<LogicModule>
    fun get(projectId: Long, name: String): LogicModule
    fun update(id: String, logicModule: LogicModule)
    fun updateAll(logicModules: List<LogicModule>)
    fun create(projectId: Long, logicModule: LogicModule)
    fun createWithCompositeNodes(projectId: Long, logicModule: LogicModuleWithCompositeNodes)
    fun delete(id: String)
    fun deleteByProjectId(projectId: Long)
    fun saveAll(projectId: Long, logicModules: List<LogicModule>)
    fun getAllSubModule(projectId: Long): List<SubModule>
    fun getParentClassId(id: String): List<String>
}
