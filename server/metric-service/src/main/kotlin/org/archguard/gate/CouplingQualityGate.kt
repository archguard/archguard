package org.archguard.gate

import java.util.Date

data class CouplingQualityGate(
    var id: Long?,
    var name: String,
    var config: List<QualityGateConfig>?,
    var createdAt: Date?,
    var updatedAt: Date?
)

