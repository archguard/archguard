package org.archguard.domain.insight

import org.archguard.domain.comparison.Comparison
import org.archguard.domain.version.VersionComparison
import java.lang.RuntimeException

object ValueValidate {
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

    private fun validate(source: String, queryMode: QueryMode, filterValue: String): Boolean {
        return when (queryMode) {
            QueryMode.StrictMode -> source == filterValue
            QueryMode.RegexMode -> source.matches(filterValue.toRegex())
            QueryMode.LikeMode -> {
                // like type was already used in query, **NOT** need to be implemented. If run to here, it's a bug
                throw RuntimeException("Query validate encountered LikeMode, this is likely a bug, please report to github issues page.")
            }
            else -> { false }
        }
    }

    fun isValueValid(source: String, filter: Pair<FilterValue, QueryMode>?): Boolean {
        if (filter == null) return true

        return validate(source, filter.second, filter.first)
    }
}