package org.archguard.scanner.cost.estimate;

import org.archguard.scanner.cost.estimate.Estimate
import org.archguard.scanner.cost.estimate.EstimateCost
import kotlin.math.pow

// Basic COCOMO Params from Boehm
//
// Organic – A software project is said to be an organic type if the team size required is adequately small, the
// problem is well understood and has been solved in the past and also the team members have a nominal experience
// regarding the problem.
// Semi-detached – A software project is said to be a Semi-detached type if the vital characteristics such as team-size,
// experience, knowledge of the various programming environment lie in between that of organic and Embedded.
// The projects classified as Semi-Detached are comparatively less familiar and difficult to develop compared to
// the organic ones and require more experience and better guidance and creativity. Eg: Compilers or
// different Embedded Systems can be considered of Semi-Detached type.
// Embedded – A software project with requiring the highest level of complexity, creativity, and experience
// requirement fall under this category. Such software requires a larger team size than the other two models
// and also the developers need to be sufficiently experienced and creative to develop such complex models.

// https://en.wikipedia.org/wiki/COCOMO
var projectType = mapOf(
    "organic" to arrayOf(2.4, 1.05, 2.5, 0.38),
    "semi-detached" to arrayOf(3.0, 1.12, 2.5, 0.35),
    "embedded" to arrayOf(3.6, 1.20, 2.5, 0.32),
)
var CocomoProjectType = "organic"



// AverageWage is the average wage in dollars used for the COCOMO cost estimate
// var AverageWage int64 = 56286
const val AverageWage = 56286

// Overhead is the overhead multiplier for corporate overhead (facilities, equipment, accounting, etc.)
// var Overhead float64 = 2.4
const val Overhead = 2.4

// Effort Adjustment Factor (EAF) = 1.00
// Total Equivalent Size = 0 SLOC
class CocomoEstimate : Estimate {
    var EAF: Double = 1.0

    override fun estimate(code: Int): EstimateCost {
        val estimatedEffort = effort(code, EAF)
        val estimatedCost = cost(estimatedEffort, AverageWage, Overhead)
        val estimatedScheduleMonths = months(estimatedEffort)
        val estimatedPeopleRequired = estimatedEffort / estimatedScheduleMonths
        return EstimateCost(estimatedCost, estimatedScheduleMonths, estimatedPeopleRequired)
    }

    companion object {
        fun cost(effortApplied: Double, averageWage: Int, overhead: Double): Double {
            return effortApplied * (averageWage / 12) * overhead
        }

        fun effort(sloc: Int, eaf: Double): Double {
            return projectType[CocomoProjectType]!![0] * (sloc.toDouble() / 1000).pow(projectType[CocomoProjectType]!![1]) * eaf
        }

        fun months(effortApplied: Double): Double {
            return projectType[CocomoProjectType]!![2] * effortApplied.pow(projectType[CocomoProjectType]!![3])
        }
    }

}