package com.thoughtworks.archguard.dependence.domain.logic_module

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogicModuleService {
    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Autowired
    lateinit var baseModuleRepository: BaseModuleRepository

    fun getLogicModules(): List<LogicModule> {
        return logicModuleRepository.getAll()
    }

    fun updateLogicModule(id: String, logicModule: LogicModule) {
        logicModuleRepository.update(id, logicModule)
    }

    fun createLogicModule(logicModule: LogicModule): String {
        var id = UUID.randomUUID().toString()
        logicModule.id = id
        logicModuleRepository.create(logicModule)
        return id
    }

    fun deleteLogicModule(id: String) {
        logicModuleRepository.delete(id)
    }

    fun getLogicModulesDependencies(caller: String, callee: String): List<ModuleDependency> {
        return logicModuleRepository.getDependence(caller, callee)
    }

    fun autoDefineLogicModule() {
        logicModuleRepository.deleteAll()
        var defaultModules = baseModuleRepository.getBaseModules().map { it -> LogicModule(UUID.randomUUID().toString(), it, mutableListOf(it)) }
        logicModuleRepository.saveAll(defaultModules)
    }


}
