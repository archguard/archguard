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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/logic-modules")
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
    fun getLogicModules(): List<LogicModuleLegacy> {
        return logicModuleService.getLogicModules().filter { it.isLogicModule() }.map { LogicModuleLegacy.fromLogicModule(it) }
    }

    @PostMapping("/hide-all")
    fun hideAllLogicModules() {
        logicModuleService.hideAllLogicModules()
    }

    @PostMapping("/show-all")
    fun showAllLogicModules() {
        logicModuleService.showAllLogicModules()
    }

    @PostMapping("/reverse-all")
    fun reverseAllLogicModules() {
        logicModuleService.reverseAllLogicModulesStatus()
    }

    @PutMapping("/{id}")
    fun updateLogicModule(@PathVariable id: String, @RequestBody logicModule: LogicModuleLegacy) {
        logicModuleService.updateLogicModule(id, logicModule.toLogicModule())
    }

    @PostMapping
    fun createLogicModule(@RequestBody logicModule: LogicModuleLegacy): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModule(logicModule.toLogicModule())
    }

    @PostMapping("/service")
    fun createLogicModuleWithCompositeNodes(@RequestBody logicModule: LogicModuleWithCompositeNodes): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModuleWithCompositeNodes(logicModule.toLogicModule())
    }

    @DeleteMapping("/{id}")
    fun deleteLogicModule(@PathVariable id: String) {
        logicModuleService.deleteLogicModule(id)
    }

    @PostMapping("{projectId}/auto-define")
    @ResponseStatus(HttpStatus.OK)
    fun autoDefineLogicModule(@PathVariable projectId: Long) {
        logicModuleService.autoDefineLogicModule(projectId)
    }

    @GetMapping("/dependencies")
    fun getLogicModulesDependencies(@RequestParam caller: String, @RequestParam callee: String): List<Dependency<JMethodVO>> {
        return dependencyService.getAllMethodDependencies(caller, callee)
    }

    @PostMapping("/calculate-coupling")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated("Remove in the future")
    fun calculateCoupling() {
        metricsService.calculateCouplingLegacy()
    }

    @GetMapping("/metrics")
    @Deprecated("Remove in the future")
    fun getAllMetrics(): List<ModuleMetricsLegacy> {
        return metricsService.getAllMetricsLegacy()
    }

    @GetMapping("/metrics/modules")
    fun getModuleMetrics(): List<ModuleMetricsLegacy> {
        return metricsService.getModuleMetricsLegacy()
    }

    @GetMapping("/dependencies/graph")
    fun getDependenciesGraph(): Graph {
        return graphService.getLogicModuleGraph()
    }

}

