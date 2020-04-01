package com.thoughtworks.archgard.scanner.domain.config.model

data class ToolConfigure(var type: String, var configs: Map<String, String>)

fun getConfigNames(toolConfigure: ToolConfigure): List<String> {
    val names = ArrayList<String>()
    toolConfigure.configs.forEach {
        names.add(toolConfigure.type + '-' + it.key)
    }
    return names
}