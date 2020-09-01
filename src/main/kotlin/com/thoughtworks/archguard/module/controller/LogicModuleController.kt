package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.metrics.appl.MetricsService
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetricsLegacy
import com.thoughtworks.archguard.module.domain.LogicModuleService
import com.thoughtworks.archguard.module.domain.LogicModuleWithCompositeNodes
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.graph.Graph
import com.thoughtworks.archguard.module.domain.graph.GraphService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.infrastructure.dto.LogicModuleLegacy
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
@RequestMapping("/systems/{systemId}/logic-modules")
class LogicModuleController(val logicModuleService: LogicModuleService, val dependencyService: DependencyService,
                            val metricsService: MetricsService, val graphService: GraphService) {
    @GetMapping
    fun getLogicModules(@PathVariable("systemId") systemId: Long): List<LogicModuleLegacy> {
        return logicModuleService.getLogicModules(systemId)
                .filter { it.isLogicModule() }.map { LogicModuleLegacy.fromLogicModule(it) }
    }

    @PostMapping("/hide-all")
    fun hideAllLogicModules(@PathVariable("systemId") systemId: Long) {
        logicModuleService.hideAllLogicModules(systemId)
    }

    @PostMapping("/show-all")
    fun showAllLogicModules(@PathVariable("systemId") systemId: Long) {
        logicModuleService.showAllLogicModules(systemId)
    }

    @PostMapping("/reverse-all")
    fun reverseAllLogicModules(@PathVariable("systemId") systemId: Long) {
        logicModuleService.reverseAllLogicModulesStatus(systemId)
    }

    @PutMapping("/{id}")
    fun updateLogicModule(@PathVariable("systemId") systemId: Long,
                          @PathVariable id: String,
                          @RequestBody logicModule: LogicModuleLegacy) {
        logicModuleService.updateLogicModule(systemId, id, logicModule.toLogicModule())
    }

    @PostMapping
    fun createLogicModule(@PathVariable("systemId") systemId: Long,
                          @RequestBody logicModule: LogicModuleLegacy): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModule(systemId, logicModule.toLogicModule())
    }

    @PostMapping("/service")
    fun createLogicModuleWithCompositeNodes(@PathVariable("systemId") systemId: Long,
                                            @RequestBody logicModule: LogicModuleWithCompositeNodes): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModuleWithCompositeNodes(systemId, logicModule.toLogicModule())
    }

    @DeleteMapping("/{id}")
    fun deleteLogicModule(@PathVariable("systemId") systemId: Long,
                          @PathVariable id: String) {
        logicModuleService.deleteLogicModule(systemId, id)
    }

    @PostMapping("/auto-define")
    @ResponseStatus(HttpStatus.OK)
    fun autoDefineLogicModule(@PathVariable systemId: Long) {
        logicModuleService.autoDefineLogicModule(systemId)
    }

    @GetMapping("/dependencies")
    fun getLogicModulesDependencies(@PathVariable systemId: Long,
                                    @RequestParam caller: String,
                                    @RequestParam callee: String): List<Dependency<JMethodVO>> {
        return dependencyService.getAllMethodDependencies(systemId, caller, callee)
    }

    @PostMapping("/calculate-coupling")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated("Remove in the future")
    fun calculateCoupling(@PathVariable systemId: Long) {
        metricsService.calculateCouplingLegacy(systemId)
    }

    @GetMapping("/metrics")
    @Deprecated("Remove in the future")
    fun getAllMetrics(@PathVariable systemId: Long): List<ModuleMetricsLegacy> {
        return metricsService.getAllMetricsLegacy(systemId)
    }

    @GetMapping("/metrics/modules")
    @Deprecated("Remove in the future")
    fun getModuleMetrics(@PathVariable systemId: Long): List<ModuleMetricsLegacy> {
        return metricsService.getAllMetricsLegacy(systemId)
    }

    @GetMapping("/dependencies/graph")
    fun getDependenciesGraph(@PathVariable systemId: Long): Graph {
        return graphService.getLogicModuleGraph(systemId)
    }

}

