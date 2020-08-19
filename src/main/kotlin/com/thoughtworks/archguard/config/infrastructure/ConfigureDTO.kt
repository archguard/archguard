package com.thoughtworks.archguard.config.infrastructure

import com.thoughtworks.archguard.config.domain.Configure
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ConfigureDTO(
        val id: String?,
        @ColumnName("project_id") val projectId: Long,
        val type: String,
        val key: String,
        val value: String,
        val order: Int
) {
    companion object {
        fun of(configure: Configure) = ConfigureDTO(
                configure.id,
                configure.projectId,
                configure.type,
                configure.key,
                configure.value,
                configure.order
        )
    }

    fun toDomainObject() = Configure(id, projectId, type, key, value, order)
}