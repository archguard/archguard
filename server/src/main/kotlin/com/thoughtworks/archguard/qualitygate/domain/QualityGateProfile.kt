package com.thoughtworks.archguard.qualitygate.domain

import org.archguard.json.JsonUtils
import java.util.Date

data class QualityGateProfile(
    var id: Long?,
    val name: String,
    val config: String,
    val createdAt: Date,
    var updatedAt: Date
) {
    constructor(name: String, config: String) : this(null, name, config, Date(), Date())

    companion object {
        fun default(): QualityGateProfile {
            return QualityGateProfile("", "[]")
        }
    }

    fun toDto(): QualityGateProfileDTO {
        val qualityGateConfig = JsonUtils.json2obj<List<QualityGateConfig>>(config)
        return QualityGateProfileDTO(id, name, qualityGateConfig, createdAt, updatedAt)
    }
}
