package com.thoughtworks.archgard.scanner2.domain.model

class ClassCoupling(
        val jClassVO: JClassVO,
        val innerFanIn: Int,
        val innerFanOut: Int,
        val outerFanIn: Int,
        val outerFanOut: Int) {
    val innerInstability: Double = if (innerFanIn + innerFanOut == 0) 0.0 else innerFanOut.toDouble() / (innerFanOut + innerFanIn)
    val innerCoupling: Double = if (innerFanIn + innerFanOut == 0) 0.0 else 1 - 1.0 / (innerFanOut + innerFanIn)
    val outerInstability: Double = if (outerFanIn + outerFanOut == 0) 0.0 else outerFanOut.toDouble() / (outerFanOut + outerFanIn)
    val outerCoupling: Double = if (outerFanIn + outerFanOut == 0) 0.0 else 1 - 1.0 / (outerFanOut + outerFanIn)
}
