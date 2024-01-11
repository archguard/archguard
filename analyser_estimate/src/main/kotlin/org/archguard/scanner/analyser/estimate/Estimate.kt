package org.archguard.scanner.analyser.estimate

/**
 * The Estimate interface represents an estimate for the cost of a given code.
 * It provides a method to estimate the cost based on the provided code.
 */
interface Estimate {
    /**
     * Estimates the cost of the given code.
     *
     * @param code the code for which the cost needs to be estimated
     * @return an EstimateCost object representing the estimated cost
     */
    fun estimate(code: Int): EstimateCost
}

data class EstimateCost(
    val cost: Double,
    val month: Double,
    val people: Double,
)
