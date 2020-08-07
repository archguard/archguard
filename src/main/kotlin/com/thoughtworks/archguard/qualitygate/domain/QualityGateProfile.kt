package com.thoughtworks.archguard.qualitygate.domain

import com.thoughtworks.archguard.common.JsonUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


data class QualityGateProfile(var id: String?,
                              val name: String,
                              val config: List<QualityGateConfig>,
                              val createdAt: LocalDateTime?,
                              var updatedAt: LocalDateTime?) {
    fun toDto(): QualityGateProfileDTO {
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return QualityGateProfileDTO(id ?: UUID.randomUUID().toString(),
                name,
                JsonUtils.obj2json(config),
                (createdAt ?: LocalDateTime.now()).format(pattern),
                (updatedAt ?: LocalDateTime.now()).format(pattern))
    }
}

