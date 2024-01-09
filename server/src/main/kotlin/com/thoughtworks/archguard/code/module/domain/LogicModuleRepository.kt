package com.thoughtworks.archguard.code.module.domain

import org.archguard.arch.LogicModule
import com.thoughtworks.archguard.code.module.domain.model.SubModule

interface LogicModuleRepository {
    fun getAllByShowStatus(systemId: Long, isShow: Boolean): List<LogicModule>
    fun getAllBySystemId(systemId: Long): List<LogicModule>
    fun get(systemId: Long, name: String): LogicModule
    fun update(id: String, logicModule: LogicModule)
    fun updateAll(logicModules: List<LogicModule>)
    fun create(systemId: Long, logicModule: LogicModule)
    fun createWithCompositeNodes(systemId: Long, logicModule: LogicModuleWithCompositeNodes)
    fun delete(id: String)
    fun deleteBySystemId(systemId: Long)
    fun saveAll(systemId: Long, logicModules: List<LogicModule>)
    fun getAllSubModule(systemId: Long): List<SubModule>
    fun getParentClassId(id: String): List<String>
}
