package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.GraphService
import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.LogicModuleService
import com.thoughtworks.archguard.module.domain.ModuleCouplingReport
import com.thoughtworks.archguard.module.domain.ModuleCouplingReportDTO
import com.thoughtworks.archguard.module.domain.ModuleDependency
import com.thoughtworks.archguard.module.domain.ModuleGraph
import org.springframework.beans.factory.annotation.Autowired
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
    private lateinit var graphService: GraphService

    @GetMapping
    fun getLogicModules(): List<LogicModule> {
        return logicModuleService.getLogicModules()
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
    fun updateLogicModule(@PathVariable id: String, @RequestBody logicModule: LogicModule) {
        logicModuleService.updateLogicModule(id, logicModule)
    }

    @PostMapping
    fun createLogicModule(@RequestBody logicModule: LogicModule): String {
        return logicModuleService.createLogicModule(logicModule)
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
        return logicModuleService.getLogicModulesDependencies(caller, callee)
    }

    @GetMapping("/graph")
    fun getLogicModuleGraph(): ModuleGraph {
        return graphService.getLogicModuleGraph()
    }

    @GetMapping("/coupling-by-class")
    fun getLogicModuleCouplingByClass(): List<ModuleCouplingReportDTO> {
        return logicModuleService.getLogicModuleCouplingReport()
    }

    @GetMapping("/coupling-detail")
    fun getLogicModuleCouplingDetail(): List<ModuleCouplingReport> {
        return logicModuleService.getLogicModuleCouplingReportDetail()
    }

}

