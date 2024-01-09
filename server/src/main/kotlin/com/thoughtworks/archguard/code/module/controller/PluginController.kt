package com.thoughtworks.archguard.code.module.controller

import org.archguard.plugin.PluginType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/plugin")
class PluginController {

    @GetMapping("/type")
    fun getPluginTypes(): List<String> {
        return PluginType.values().map { it.name }
    }
}
