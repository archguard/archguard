package com.thoughtworks.archguard.qualitygate.domain

data class QualityGateConfig(var layer: String,
                             var quota: String,
                             var operator: ComparationOperator,
                             var value: Number) {
    constructor(): this("", "", ComparationOperator.EQUAL, 0)
}
