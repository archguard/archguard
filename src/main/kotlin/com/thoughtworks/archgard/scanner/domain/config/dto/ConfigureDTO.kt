package com.thoughtworks.archgard.scanner.domain.config.dto

data class ConfigureDTO(var id: String, var type: String, var key: String, var value: String) {
    fun getConfigName(): String {
        return "$type-$key"
    }
}
