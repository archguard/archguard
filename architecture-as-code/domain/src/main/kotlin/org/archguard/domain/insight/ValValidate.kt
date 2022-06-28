package org.archguard.domain.insight

import org.archguard.domain.comparison.Comparison
import org.archguard.domain.version.VersionComparison

object ValValidate {
    fun isVersionValid(
        leftVersion: String,
        versionComparison: VersionComparison,
        versionFilter: Pair<FilterValue, Comparison>?,
    ): Boolean {
        if (versionFilter == null) {
            return true
        }

        val comparison = versionFilter.second
        val insightVersion = versionFilter.first
        return versionComparison.eval(leftVersion, comparison, insightVersion)
    }

    private fun validate(source: String, type: FilterType, filterValue: String): Boolean {
        return when (type) {
            FilterType.NORMAL -> source == filterValue
            FilterType.REGEXP -> source.matches(filterValue.toRegex())
            else -> false
        }
    }

    fun isValueValid(source: String, nameFilter: Pair<FilterValue, FilterType>?): Boolean {
        if (nameFilter == null) return true

        return validate(source, nameFilter.second, nameFilter.first)
    }
}