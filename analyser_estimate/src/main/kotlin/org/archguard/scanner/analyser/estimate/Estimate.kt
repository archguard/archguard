package org.archguard.scanner.analyser.estimate

interface Estimate {
    fun estimate(code: Int): EstimateCost
}


data class EstimateCost(
    val cost: Double,
    val month: Double,
    val people: Double,
)
