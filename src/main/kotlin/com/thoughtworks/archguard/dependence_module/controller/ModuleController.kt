package com.thoughtworks.archguard.dependence_module.controller

import com.thoughtworks.archguard.dependence_module.domain.dto.ModuleGraph
import com.thoughtworks.archguard.dependence_module.domain.service.ModuleService
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