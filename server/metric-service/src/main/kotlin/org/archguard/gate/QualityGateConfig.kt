package org.archguard.gate

data class QualityGateConfig(
    var layer: LayerType,
    var quota: String,
    var operator: ComparatorOperator,
    var value: Number
) {
    constructor() : this(LayerType.MODULE, "", ComparatorOperator.EQUAL, 0)
}