package com.thoughtworks.archguard.code.module.controller

import com.thoughtworks.archguard.code.module.domain.LogicModuleService
import com.thoughtworks.archguard.code.module.domain.LogicModuleWithCompositeNodes
import com.thoughtworks.archguard.code.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.code.module.domain.graph.GraphService
import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import com.thoughtworks.archguard.code.module.infrastructure.dto.LogicModuleLegacy
import org.archguard.graph.Graph
import org.archguard.model.Dependency
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
import java.util.UUID

@RestController
@RequestMapping("/api/systems/{systemId}/logic-module")
class LogicModuleController(
    val logicModuleService: LogicModuleService,
    val dependencyService: DependencyService,
    val graphService: GraphService
) {
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
    fun updateLogicModule(
        @PathVariable("systemId") systemId: Long,
        @PathVariable id: String,
        @RequestBody logicModule: LogicModuleLegacy
    ) {
        logicModuleService.updateLogicModule(systemId, id, logicModule.toLogicModule())
    }

    @PostMapping
    fun createLogicModule(
        @PathVariable("systemId") systemId: Long,
        @RequestBody logicModule: LogicModuleLegacy
    ): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModule(systemId, logicModule.toLogicModule())
    }

    @PostMapping("/service")
    fun createLogicModuleWithCompositeNodes(
        @PathVariable("systemId") systemId: Long,
        @RequestBody logicModule: LogicModuleWithCompositeNodes
    ): String {
        logicModule.id = UUID.randomUUID().toString()
        return logicModuleService.createLogicModuleWithCompositeNodes(systemId, logicModule.toLogicModule())
    }

    @DeleteMapping("/{id}")
    fun deleteLogicModule(
        @PathVariable("systemId") systemId: Long,
        @PathVariable id: String
    ) {
        logicModuleService.deleteLogicModule(systemId, id)
    }

    @PostMapping("/auto-define")
    @ResponseStatus(HttpStatus.OK)
    fun autoDefineLogicModule(@PathVariable systemId: Long) {
        logicModuleService.autoDefineLogicModule(systemId)
    }

    @GetMapping("/dependencies")
    fun getLogicModulesDependencies(
        @PathVariable systemId: Long,
        @RequestParam caller: String,
        @RequestParam callee: String
    ): List<Dependency<JMethodVO>> {
        return dependencyService.getAllDistinctMethodDependencies(systemId, caller, callee)
    }

    @GetMapping("/dependencies/graph")
    fun getDependenciesGraph(@PathVariable systemId: Long): Graph {
        return graphService.getLogicModuleGraph(systemId)
    }
}
