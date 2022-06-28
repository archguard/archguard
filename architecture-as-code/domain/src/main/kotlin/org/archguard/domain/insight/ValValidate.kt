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

        return versionComparison.eval(leftVersion, versionFilter.second, versionFilter.first)
    }

    private fun validate(source: String, type: FilterType, filterValue: String): Boolean {
        return when (type) {
            FilterType.NORMAL -> source == filterValue
            FilterType.REGEXP -> source.matches(filterValue.toRegex())
            FilterType.LIKE -> {
                // like type was already used in query, **NOT** need to be implemented. If run to here, it's a bug
                TODO()
            }
        }
    }

    fun isValueValid(source: String, filter: Pair<FilterValue, FilterType>?): Boolean {
        if (filter == null) return true

        return validate(source, filter.second, filter.first)
    }
}