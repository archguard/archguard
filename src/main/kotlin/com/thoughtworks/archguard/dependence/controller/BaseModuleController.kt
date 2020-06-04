package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
