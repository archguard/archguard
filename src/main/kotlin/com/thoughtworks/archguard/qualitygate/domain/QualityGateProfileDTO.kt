package com.thoughtworks.archguard.qualitygate.domain

import com.thoughtworks.archguard.common.JsonUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class QualityGateProfileDTO(var id: String,
                                 var name: String,
                                 var config: String,
                                 var createdAt: String,
                                 var updatedAt: String) {
    constructor() : this("", "", "", "", "")

    fun toProfile(): QualityGateProfile {
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val qualityGateConfig = JsonUtils.json2obj<List<QualityGateConfig>>(config)
        return QualityGateProfile(id, name, qualityGateConfig,
                LocalDateTime.parse(createdAt, pattern), LocalDateTime.parse(updatedAt, pattern))
    }
}
