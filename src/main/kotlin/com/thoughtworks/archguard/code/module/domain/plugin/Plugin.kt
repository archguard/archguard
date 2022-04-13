package com.thoughtworks.archguard.code.module.domain.plugin

interface Plugin {
    fun getPluginType() : PluginType
}