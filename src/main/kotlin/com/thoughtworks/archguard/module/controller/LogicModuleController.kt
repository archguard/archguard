package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.DependencyService
import com.thoughtworks.archguard.module.domain.GraphService
import com.thoughtworks.archguard.module.domain.LogicModuleLegacy
import com.thoughtworks.archguard.module.domain.LogicModuleService
import com.thoughtworks.archguard.module.domain.ModuleCouplingReport
import com.thoughtworks.archguard.module.domain.ModuleCouplingReportDTO
import com.thoughtworks.archguard.module.domain.ModuleDependency
import com.thoughtworks.archguard.module.domain.ModuleGraph
import com.thoughtworks.archguard.module.domain.ReportService
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
    // FIXME
    fun getLogicModules(): List<LogicModuleLegacy> {
        return logicModuleService.getLogicModulesLegacy()
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
    fun getLogicModulesDependencies(@RequestParam caller: String, @RequestParam callee: String): List<ModuleDependency> {
        return dependencyService.getLogicModulesDependencies(caller, callee)
    }

    @GetMapping("/graph")
    fun getLogicModuleGraph(): ModuleGraph {
        return graphService.getLogicModuleGraph()
    }

    @GetMapping("/graph-dubbo")
    fun getLogicModuleGraphDubbo(): ModuleGraph {
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

}

