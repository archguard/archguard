package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects/{systemId}")
class BaseModuleController {

    @Autowired
    private lateinit var logicModuleRepository: LogicModuleRepository

    @GetMapping("/base-modules")
    fun getBaseModules(@PathVariable("systemId") systemId: Long): List<String> {
        return logicModuleRepository.getAllSubModule(systemId).map { it.name }
    }
}
