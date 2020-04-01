package com.thoughtworks.archgard.scanner.domain.config.model

data class ToolConfigure(var type: String, var configs: Map<String, String>) {
    fun getConfigNames(): List<String> {
        val names = ArrayList< String>()
        configs.forEach {
            names.add(type + '-' + it.key)
        }
        return names
    }
}
