package com.thoughtworks.archguard.metrics.domain.coupling

@Deprecated("")
data class ClassMetricsLegacy(
        var id: Long?,
        var packageId: Long,
        var className: String,
        var innerFanIn: Int,
        var innerFanOut: Int,
        var outerFanIn: Int,
        var outerFanOut: Int,
        var innerInstability: Double,
        var innerCoupling: Double,
        var outerInstability: Double,
        var outerCoupling: Double
) {
    constructor() : this(null, 0, "", 0, 0, 0,
            0, 0.0, 0.0, 0.0, 0.0)

    companion object {
        fun of(className: String, innerFanIn: Int, innerFanOut: Int, outerFanIn: Int, outerFanOut: Int) = ClassMetricsLegacy(
                id = null,
                packageId = 0,
                className = className,
                innerFanIn = innerFanIn,
                innerFanOut = innerFanOut,
                outerFanIn = outerFanIn,
                outerFanOut = outerFanOut,
                innerInstability = if (innerFanIn + innerFanOut == 0) 0.0 else innerFanOut.toDouble() / (innerFanOut + innerFanIn),
                innerCoupling = if (innerFanIn + innerFanOut == 0) 0.0 else 1 - 1.0 / (innerFanOut + innerFanIn),
                outerInstability = if (outerFanIn + outerFanOut == 0) 0.0 else outerFanOut.toDouble() / (outerFanOut + outerFanIn),
                outerCoupling = if (outerFanIn + outerFanOut == 0) 0.0 else 1 - 1.0 / (outerFanOut + outerFanIn)
        )
    }

}