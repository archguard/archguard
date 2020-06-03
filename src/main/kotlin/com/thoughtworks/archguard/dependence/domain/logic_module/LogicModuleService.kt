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
        val results = getModuleDependency()

        val moduleStore = ModuleStore()
        results
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun getModuleDependency(): List<ModuleGraphDependency> {
        val modules = logicModuleRepository.getAll()
        val members = modules.map { it.members }.flatten()
        val results = logicModuleRepository.getAllDependence(members)

        mapToModule(results, modules)
        return results
    }

    private fun mapToModule(results: List<ModuleGraphDependency>, modules: List<LogicModule>) {
        // TODO[如果module配置时有重叠部分怎么办]
        results.forEach {
            it.caller = modules
                    .filter { logicModule -> logicModule.members.any { j -> it.caller.startsWith(j) } }
                    .map { logicModule -> logicModule.name }[0]
            it.callee = modules
                    .filter { logicModule -> logicModule.members.any { j -> it.callee.startsWith(j) } }
                    .map { logicModule -> logicModule.name }[0]
        }
    }

    fun getLogicModuleCoupling(): ModuleCoupling {
        val moduleDependency = getModuleDependency()
        val reports = getLogicModules().map { getModuleCouplingReport(it, moduleDependency) }
        return ModuleCoupling(reports)
    }

    fun getModuleCouplingReport(module: LogicModule,
                                moduleDependency: List<ModuleGraphDependency>): ModuleCouplingReport {
        val fanOut = moduleDependency.filter { it.caller == module.name }.count()
        val fanIn = moduleDependency.filter { it.callee == module.name }.count()
        return ModuleCouplingReport(module.name, fanIn, fanOut)
    }

}

data class ModuleCouplingReport(val module: String,
                                val fanIn: Int,
                                val fanOut: Int) {
    val moduleInstability: Double = fanOut.toDouble() / (fanOut + fanIn)
    val moduleCoupling: Double = 1 - 1.0 / (fanOut + fanIn)

    constructor() : this("", 0, 0)
}
