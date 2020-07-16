package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.QuerySubModuleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class BaseModuleController {

    @Autowired
    private lateinit var querySubModuleService: QuerySubModuleService

    @GetMapping("/base-modules")
    fun getBaseModules(): List<String> {
        return querySubModuleService.getBaseModules().map { it.name }
    }
}
