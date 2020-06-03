package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModule
import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModuleService
import com.thoughtworks.archguard.dependence.domain.logic_module.ModuleDependency
import com.thoughtworks.archguard.dependence.domain.logic_module.ModuleGraph
import com.thoughtworks.archguard.dependence.domain.logic_module.ModuleCoupling
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/logic-modules")
class LogicModuleController {

    @Autowired
    private lateinit var logicModuleService: LogicModuleService

    @GetMapping
    fun getLogicModules(): List<LogicModule> {
        return logicModuleService.getLogicModules()
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

    @GetMapping("/dependencies")
    fun getLogicModulesDependencies(@RequestParam caller: String, @RequestParam callee: String): List<ModuleDependency> {
        return logicModuleService.getLogicModulesDependencies(caller, callee)
    }

    @GetMapping("/graph")
    fun getLogicModuleGraph(): ModuleGraph {
        return logicModuleService.getLogicModuleGraph()
    }


    fun getLogicModuleCoupling(): ModuleCoupling {
        return logicModuleService.getLogicModuleCoupling()
    }
}

