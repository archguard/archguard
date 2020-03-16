package com.thoughtworks.archguard.dependence_module.domain.service

import com.thoughtworks.archguard.dependence_module.domain.dto.ModuleDependenceDTO
import com.thoughtworks.archguard.dependence_module.domain.dto.ModuleGraph
import com.thoughtworks.archguard.dependence_module.domain.repository.ModuleRepository
import com.thoughtworks.archguard.dependence_module.domain.store.ModuleStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModuleService {
    @Autowired
    lateinit var moduleRepository: ModuleRepository

    fun getModuleDependence(): ModuleGraph {
        val moduleMap = getModuleDefines()

        val packagesList = moduleMap.map { it.value }.flatten()
        val results = moduleRepository.getModuleDependence(packagesList)
        mapToModule(results, moduleMap)

        val moduleStore = ModuleStore()
        results
                .groupBy { it.aModule }
                .forEach {
                    it.value.groupBy { i -> i.bModule }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun mapToModule(results: List<ModuleDependenceDTO>, moduleMap: java.util.HashMap<String, List<String>>) {
        // TODO[如果module配置时有重叠部分怎么办]
        results.forEach {
            it.aModule = moduleMap
                    .filter { i -> i.value.any { j -> it.aModule.startsWith(j) } }
                    .map { i -> i.key }[0]
            it.bModule = moduleMap
                    .filter { i -> i.value.any { j -> it.bModule.startsWith(j) } }
                    .map { i -> i.key }[0]
        }
    }

    private fun getModuleDefines(): HashMap<String, List<String>> {
        val moduleMap = HashMap<String, List<String>>()
        moduleRepository.getModulePackages().forEach { moduleMap[it.name] = it.packages.split(',') }
        return moduleMap
    }
}
