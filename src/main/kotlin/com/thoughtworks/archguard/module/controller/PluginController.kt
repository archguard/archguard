package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.plugin.PluginType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/plugin")
class PluginController {

    @GetMapping("/type")
    fun getPluginTypes(): List<String> {
        return PluginType.values().map { it.name }
    }
}
