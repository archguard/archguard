package com.thoughtworks.archguard.dependence.domain.logic_module

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

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
        val id = UUID.randomUUID().toString()
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
        val defaultModules = baseModuleRepository.getBaseModules().map { it -> LogicModule(UUID.randomUUID().toString(), it, mutableListOf(it)) }
        logicModuleRepository.saveAll(defaultModules)
    }

    fun getLogicModuleGraph(): ModuleGraph {
        val moduleMap = HashMap<String, List<String>>()
        logicModuleRepository.getAll().forEach { moduleMap[it.name] = it.members }
        val membersList = moduleMap.map { it.value }.flatten()
        val results = logicModuleRepository.getAllDependence(membersList)

        mapToModule(results, moduleMap)

        val moduleStore = ModuleStore()
        results
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun mapToModule(results: List<ModuleGraphDependency>, moduleMap: java.util.HashMap<String, List<String>>) {
        // TODO[如果module配置时有重叠部分怎么办]
        results.forEach {
            it.caller = moduleMap
                    .filter { i -> i.value.any { j -> it.caller.startsWith(j) } }
                    .map { i -> i.key }[0]
            it.callee = moduleMap
                    .filter { i -> i.value.any { j -> it.callee.startsWith(j) } }
                    .map { i -> i.key }[0]
        }
    }



}
