package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.sizing.FilterSizing

data class FilterSizingPO(
    val limit: Long,
    val offset: Long,
    val module: String,
    val className: String,
    val packageName: String,
    val name: String
) {

    companion object {
        fun fromFilterSizing(filterSizing: FilterSizing): FilterSizingPO {
            return FilterSizingPO(
                filterSizing.limit,
                filterSizing.offset,
                filterSizing.module,
                filterSizing.className,
                filterSizing.packageName,
                filterSizing.name
            )
        }
    }
}
