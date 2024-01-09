package com.thoughtworks.archguard.qualitygate.domain

import org.archguard.json.JsonUtils
import java.util.Date

data class QualityGateProfileDTO(
    var id: Long?,
    var name: String,
    var config: List<QualityGateConfig>,
    var createdAt: Date?,
    var updatedAt: Date?
) {
    constructor() : this(null, "", emptyList(), Date(), Date())

    fun toProfile(): QualityGateProfile {
        return QualityGateProfile(
            id,
            name,
            JsonUtils.obj2json(config),
            createdAt ?: Date(),
            updatedAt ?: Date()
        )
    }
}
