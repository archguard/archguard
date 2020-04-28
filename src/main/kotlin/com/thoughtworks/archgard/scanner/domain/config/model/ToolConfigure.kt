package com.thoughtworks.archgard.scanner.domain.config.model

data class ToolConfigure(var type: String, var configs: Map<String, String>) {
    fun getConfigNames(): List<String> {
        return configs.map { "${type}-${it.key}" }
    }
}
