package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.metrics.domain.MetricsService
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetricsLegacy
import com.thoughtworks.archguard.module.domain.LogicModuleService
import com.thoughtworks.archguard.module.domain.LogicModuleWithCompositeNodes
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.graph.Graph
import com.thoughtworks.archguard.module.domain.graph.GraphService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.infrastructure.dto.LogicModuleLegacy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/projects/{projectId}/logic-modules")
class LogicModuleController {

    @Autowired
    private lateinit var logicModuleService: LogicModuleService

    @Autowired
    private lateinit var dependencyService: DependencyService

    @Autowired
    private lateinit var metricsService: MetricsService

    @Autowired
    private lateinit var graphService: GraphService

    @GetMapping
    fun getLogicModules(@PathVariable("projectId") projectId: Long): List<LogicModuleLegacy> {
        return logicModuleService.getLogicModules(projectId)
                .filter { it.isLogicModule() }.map { LogicModuleLegacy.fromLogicModule(it) }
    }

    @PostMapping("/hide-all")
    fun hideAllLogicModules(@PathVariable("projectId") projectId: Long) {
        logicModuleService.hideAllLogicModules(projectId)
    }

    @PostMapping("/show-all")
    fun showAllLogicModules(@PathVariable("projectId") projectId: Long) {
        logicModuleService.showAllLogicModules(projectId)
    }

    @PostMapping("/reverse-all")
    fun reverseAllLogicModules(@PathVariable("projectId") projectId: Long) {
        logicModuleService.reverseAllLogicModulesStatus(projectId)
    }

    @PutMapping("/{id}")
    fun updateLogicModule(@PathVariable("projectId") projectId: Long,
                          @PathVariable id: String,
                          @RequestBody logicModule: LogicModuleLegacy) {
        logicModuleService.updateLogicModule(projectId, id, logicModule.toLogicModule())
    }

    @PostMapping
    fun createLogicModule(@PathVariable("projectId") projectId: Long,
                          @RequestBody logicModule: LogicModuleLegacy): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModule(logicModule.toLogicModule())
    }

    @PostMapping("/service")
    fun createLogicModuleWithCompositeNodes(@PathVariable("projectId") projectId: Long,
                                            @RequestBody logicModule: LogicModuleWithCompositeNodes): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModuleWithCompositeNodes(logicModule.toLogicModule())
    }

    @DeleteMapping("/{id}")
    fun deleteLogicModule(@PathVariable("projectId") projectId: Long,
                          @PathVariable id: String) {
        logicModuleService.deleteLogicModule(projectId, id)
    }

    @PostMapping("/auto-define")
    @ResponseStatus(HttpStatus.OK)
    fun autoDefineLogicModule(@PathVariable projectId: Long) {
        logicModuleService.autoDefineLogicModule(projectId)
    }

    @GetMapping("/dependencies")
    fun getLogicModulesDependencies(@PathVariable projectId: Long,
                                    @RequestParam caller: String,
                                    @RequestParam callee: String): List<Dependency<JMethodVO>> {
        return dependencyService.getAllMethodDependencies(projectId, caller, callee)
    }

    @PostMapping("/calculate-coupling")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated("Remove in the future")
    fun calculateCoupling(@PathVariable projectId: Long) {
        metricsService.calculateCouplingLegacy(projectId)
    }

    @GetMapping("/metrics")
    @Deprecated("Remove in the future")
    fun getAllMetrics(@PathVariable projectId: Long): List<ModuleMetricsLegacy> {
        return metricsService.getAllMetricsLegacy(projectId)
    }

    @GetMapping("/metrics/modules")
    fun getModuleMetrics(@PathVariable projectId: Long): List<ModuleMetricsLegacy> {
        return metricsService.getModuleMetricsLegacy(projectId)
    }

    @GetMapping("/dependencies/graph")
    fun getDependenciesGraph(@PathVariable projectId: Long): Graph {
        return graphService.getLogicModuleGraph(projectId)
    }

}

