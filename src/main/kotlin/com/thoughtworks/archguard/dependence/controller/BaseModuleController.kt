package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleService
import com.thoughtworks.archguard.dependence.domain.modules.ModuleGraph
import com.thoughtworks.archguard.dependence.domain.modules.ModuleService
import org.jetbrains.kotlin.com.intellij.icons.AllIcons
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class BaseModuleController {

    @Autowired
    private lateinit var baseModuleService: BaseModuleService

    @GetMapping("/base-modules")
    fun getBaseModules(): List<String> {
        return baseModuleService.getBaseModules()
    }
}
