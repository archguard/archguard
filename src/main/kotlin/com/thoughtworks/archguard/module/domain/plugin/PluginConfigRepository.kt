package com.thoughtworks.archguard.module.domain.plugin

interface PluginConfigRepository {
    fun getAll(): List<PluginConfig>
}
