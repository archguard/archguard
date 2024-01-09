package com.thoughtworks.archguard.config.infrastructure

import org.archguard.config.Configure
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ConfigureDTO(
    val id: String?,
    @ColumnName("system_id") val systemId: Long,
    val type: String,
    val key: String,
    val value: String,
    val order: Int
) {
    companion object {
        fun of(configure: Configure) = ConfigureDTO(
            configure.id,
            configure.systemId,
            configure.type,
            configure.key,
            configure.value,
            configure.order
        )
    }

    fun toDomainObject() = Configure(id, systemId, type, key, value, order)
}
