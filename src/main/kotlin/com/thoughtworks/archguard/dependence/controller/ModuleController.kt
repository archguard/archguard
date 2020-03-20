package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.domain.modules.ModuleGraph
import com.thoughtworks.archguard.dependence.domain.modules.ModuleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class ModuleController {

    @Autowired
    private lateinit var moduleService: ModuleService

    @GetMapping("/module/dependence/all")
    fun getModuleDependence(): ModuleGraph {
        return moduleService.getModuleDependence()
    }
}