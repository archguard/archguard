package com.thoughtworks.archguard.module.infrastructure.plugin

import com.thoughtworks.archguard.module.domain.plugin.PluginConfig
import com.thoughtworks.archguard.module.domain.plugin.PluginConfigRepository
import org.springframework.stereotype.Repository

@Repository
class PluginConfigRepositoryImpl: PluginConfigRepository {
    override fun getAll(): List<PluginConfig> {
        return listOf(PluginConfig("DubboPlugin"), PluginConfig("FeignClientPlugin"))
    }
}
