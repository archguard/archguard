package org.archguard.domain.version

/**
 * for like usage: isFit(">=", "1.2.2")
 */
class VersionComparison(private val versionNo: VersionNumber) {
    fun isFit(symbol: String, other: String): Boolean? {
        val comparison = Comparison.fromString(symbol)
        if (comparison == Comparison.NotSupport) {
            return null
        }

        val otherVersion = VersionNumber.parse(other) ?: return null

        val result = versionNo.compareTo(otherVersion)

        when {
            result == 0 && comparison == Comparison.Equal -> {
                return true
            }

            result > 0 && comparison == Comparison.GreaterThan -> {
                return true
            }

            result < 0 && comparison == Comparison.LessThan -> {
                return true
            }

            result >= 0 && comparison == Comparison.GreaterThanOrEqual -> {
                return true
            }

            result <= 0 && comparison == Comparison.LessThanOrEqual -> {
                return true
            }

            else -> return false
        }

    }
}