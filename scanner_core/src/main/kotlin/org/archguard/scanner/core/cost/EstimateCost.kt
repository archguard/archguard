package org.archguard.scanner.core.cost

import kotlinx.serialization.Serializable

@Serializable
data class EstimateCost (
    val cost: Float,
    val month: Float,
    val people: Float,
)
