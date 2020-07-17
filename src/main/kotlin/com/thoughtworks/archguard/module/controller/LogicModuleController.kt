package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.GraphService
import com.thoughtworks.archguard.module.domain.LogicModuleService
import com.thoughtworks.archguard.module.domain.ModuleCouplingReport
import com.thoughtworks.archguard.module.domain.ModuleCouplingReportDTO
import com.thoughtworks.archguard.module.domain.ReportService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.Graph
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.ModuleGraph
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.infrastructure.dto.LogicModuleLegacy
import com.thoughtworks.archguard.module.infrastructure.dto.MethodDependencyDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/logic-modules")
class LogicModuleController {

    @Autowired
    private lateinit var logicModuleService: LogicModuleService

    @Autowired
    @Qualifier("Default")
    private lateinit var graphService: GraphService

    @Autowired
    @Qualifier("Dubbo")
    private lateinit var graphServiceDubbo: GraphService

    @Autowired
    private lateinit var reportService: ReportService

    @Autowired
    private lateinit var dependencyService: DependencyService

    @GetMapping
    fun getLogicModules(): List<LogicModuleLegacy> {
        return logicModuleService.getLogicModules().map { LogicModuleLegacy.fromLogicModule(it) }
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

    @DeleteMapping("/{id}")
    fun deleteLogicModule(@PathVariable id: String) {
        logicModuleService.deleteLogicModule(id)
    }

    @PostMapping("/auto-define")
    fun autoDefineLogicModule() {
        logicModuleService.autoDefineLogicModule()
    }

    @PostMapping("/auto-define-with-interface")
    fun autoDefineLogicModuleWithInterface() {
        logicModuleService.autoDefineLogicModuleWithInterface()
    }

    @GetMapping("/dependencies")
    fun getLogicModulesDependencies(@RequestParam caller: String, @RequestParam callee: String): List<MethodDependencyDto> {
        return dependencyService.getLogicModulesDependencies(caller, callee).map { MethodDependencyDto.fromJMethodDependency(it) }
    }

    @GetMapping("/graph")
    @Deprecated(message = "we are going to replace with getLogicModuleGraph")
    fun getLogicModuleGraphLegacy(): ModuleGraph {
        return graphService.getLogicModuleGraphLegacy()
    }

    @GetMapping("/graph-dubbo")
    @Deprecated(message = "we are going to replace with getLogicModuleGraph")
    fun getLogicModuleGraphDubboLegacy(): ModuleGraph {
        return graphServiceDubbo.getLogicModuleGraphLegacy()
    }


    @GetMapping("/graph-new")
    fun getLogicModuleGraph(): Graph<LogicModule> {
        return graphService.getLogicModuleGraph()
    }

    @GetMapping("/graph-dubbo-new")
    fun getLogicModuleGraphDubbo(): Graph<LogicModule> {
        return graphServiceDubbo.getLogicModuleGraph()
    }

    @GetMapping("/coupling-by-class")
    fun getLogicModuleCouplingByClass(): List<ModuleCouplingReportDTO> {
        return reportService.getLogicModuleCouplingReport()
    }

    @GetMapping("/coupling-detail")
    fun getLogicModuleCouplingDetail(): List<ModuleCouplingReport> {
        return reportService.getLogicModuleCouplingReportDetail()
    }


    @GetMapping("/dependencies/caller-callee")
    fun getDependenciesBetweenCallerCallee(@RequestParam caller: String, @RequestParam callee: String): List<Dependency<JMethodVO>> {
        val callerMembers = logicModuleService.getLogicModule(caller).members
        val calleeMembers = logicModuleService.getLogicModule(callee).members
        return dependencyService.getAllWithFilter(callerMembers.map { it.getFullName() }, calleeMembers.map { it.getFullName() })
    }


}

