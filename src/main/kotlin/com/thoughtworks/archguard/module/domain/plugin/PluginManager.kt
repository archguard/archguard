package com.thoughtworks.archguard.module.domain.plugin

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class PluginManager(val pluginConfigRepository: PluginConfigRepository, val applicationContext: ApplicationContext){

    fun getPlugins(): List<Plugin> {
        val pluginConfig = pluginConfigRepository.getAll()
        val plugins: MutableMap<String, Plugin> = applicationContext.getBeansOfType(Plugin::class.java)
        return pluginConfig.mapNotNull { plugins[it.name] }
    }

}
