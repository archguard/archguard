package org.archguard.domain.version

import org.archguard.domain.comparison.Comparison

/**
 * ("1.2.3", "==").toOther("1.2.3")
 */
class VersionComparison(private val versionNo: VersionNumber = VersionNumber(0, 0, 0, 0, null), symbol: String = "") {
    private var cachedVersion: HashMap<String, VersionNumber> = hashMapOf()
    private val comparison = Comparison.fromString(symbol)

    /**
     * for example: log4.1.2 >= log4.1.1, >= log4.1.1 is our filter.
     * log4.1.1 is based version, log4.1.2 is need to compare version
     */
    fun eval(left: String, symbol: String, right: String): Boolean {
        val comparison = Comparison.fromString(symbol)
        if (comparison == Comparison.NotSupport) {
            return false
        }

        return eval(left, comparison, right)
    }

    fun eval(left: String, comparison: Comparison, right: String): Boolean {
        val leftVersion = parseVersion(left) ?: return false
        val rightVersion = parseVersion(right) ?: return false

        val result = leftVersion.compareTo(rightVersion)
        return compareByResult(result, comparison)
    }

    /**
     * for example: log4.1.2 >= log4.1.1, **log4.1.2 >=**  is our filter.
      */
    fun toOther(other: String): Boolean {
        if (comparison == Comparison.NotSupport) {
            return false
        }

        val otherVersion = parseVersion(other) ?: return false

        val result = versionNo.compareTo(otherVersion)
        return compareByResult(result, comparison)
    }

    private fun parseVersion(other: String): VersionNumber? {
        if(cachedVersion.contains(other)) {
            return cachedVersion[other]
        }

        val versionNumber = VersionNumber.parse(other)
        if (versionNumber != null) {
            cachedVersion[other] = versionNumber
        }

        return versionNumber
    }

    private fun compareByResult(result: Int, comp: Comparison): Boolean {
        when {
            result == 0 && comp == Comparison.Equal -> {
                return true
            }

            result != 0 && comp == Comparison.NotEqual -> {
                return true
            }

            result > 0 && comp == Comparison.GreaterThan -> {
                return true
            }

            result < 0 && comp == Comparison.LessThan -> {
                return true
            }

            result >= 0 && comp == Comparison.GreaterThanOrEqual -> {
                return true
            }

            result <= 0 && comp == Comparison.LessThanOrEqual -> {
                return true
            }

            else -> return false
        }
    }
}