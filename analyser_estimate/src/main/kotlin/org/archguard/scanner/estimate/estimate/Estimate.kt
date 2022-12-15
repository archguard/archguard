package org.archguard.scanner.estimate.estimate

interface Estimate {
    fun estimate(code: Int): EstimateCost
}


data class EstimateCost(
    val cost: Double,
    val month: Double,
    val people: Double,
)
