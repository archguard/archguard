package com.thoughtworks.archguard.report.domain.qualitygate

import java.util.*

data class CouplingQualityGate(var id: Long?,
                               var name: String,
                               var config: List<QualityGateConfig>?,
                               var createdAt: Date?,
                               var updatedAt: Date?)

data class QualityGateConfig(var layer: LayerType,
                             var quota: String,
                             var operator: ComparationOperator,
                             var value: Number) {
    constructor() : this(LayerType.MODULE, "", ComparationOperator.EQUAL, 0)
}

enum class LayerType {
    MODULE, PACKAGE, CLASS, COUPLINGS
}

enum class ComparationOperator {
    BIGGER,
    LESS,
    EQUAL
}
