package com.thoughtworks.archguard.module.domain.metrics

data class ClassMetrics (
        val className: String,
        val innerFanIn: Int,
        val innerFanOut: Int,
        val outerFanIn: Int,
        val outerFanOut: Int
) {
    var id: Long? = null
    var packageId: Long = 0
    val innerInstability: Double = if (innerFanIn + innerFanOut == 0) 0.0 else innerFanOut.toDouble() / (innerFanOut + innerFanIn)
    val innerCoupling: Double = if (innerFanIn + innerFanOut == 0) 0.0 else 1 - 1.0 / (innerFanOut + innerFanIn)
    val outerInstability: Double = if (outerFanIn + outerFanOut == 0) 0.0 else outerFanOut.toDouble() / (outerFanOut + outerFanIn)
    val outerCoupling: Double = if (outerFanIn + outerFanOut == 0) 0.0 else 1 - 1.0 / (outerFanOut + outerFanIn)
}