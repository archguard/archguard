package com.thoughtworks.archguard.module.domain.plugin

import org.springframework.stereotype.Component

@Component
class PluginManager(val dubboPlugin: Plugin, val feignClientPlugin: Plugin) {

    fun getPlugins(): List<Plugin> {
        return listOf(dubboPlugin, feignClientPlugin)
    }
}
