package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.config.domain.ConfigureRepository
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class PluginManager(val configureRepository: ConfigureRepository, val applicationContext: ApplicationContext){

    fun getPlugins(): List<Plugin> {
        val pluginConfig = configureRepository.getConfigures().filter { it.type == "plugin" && it.key == "name" }.sortedBy { it.order }
        val plugins: MutableMap<String, Plugin> = applicationContext.getBeansOfType(Plugin::class.java)
        return pluginConfig.mapNotNull { plugins[it.value] }
    }

}
