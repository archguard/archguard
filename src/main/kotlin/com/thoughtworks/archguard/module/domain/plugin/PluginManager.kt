package com.thoughtworks.archguard.module.domain.plugin

import com.thoughtworks.archguard.config.domain.ConfigureRepository
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class PluginManager(val configureRepository: ConfigureRepository) : ApplicationContextAware {

    private val dependPluginMap: HashMap<PluginType, DependPlugin> = hashMapOf()

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        val beansOfType: Map<String, DependPlugin> = applicationContext.getBeansOfType(DependPlugin::class.java)
        beansOfType.forEach { (_: String?, value: Plugin) -> dependPluginMap[value.getPluginType()] = value }
    }

    fun <T : Plugin> getDependPlugin(pluginType: PluginType): DependPlugin {
        return dependPluginMap[pluginType]!!
    }

    fun <T : Plugin> getDependPlugin(): List<DependPlugin> {
        val pluginConfig = configureRepository.getConfigures().filter { it.type == "plugin" && it.key == "name" }.sortedBy { it.order }
        return pluginConfig.mapNotNull { dependPluginMap[PluginType.valueOf(it.value)] }
    }
}
