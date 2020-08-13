package com.thoughtworks.archguard.qualitygate.domain

data class QualityGateConfig(var layer: LayerType,
                             var quota: String,
                             var operator: ComparationOperator,
                             var value: Number) {
    constructor() : this(LayerType.MODULE, "", ComparationOperator.EQUAL, 0)
}
