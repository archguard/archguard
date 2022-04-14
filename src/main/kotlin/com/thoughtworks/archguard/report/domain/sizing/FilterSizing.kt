package com.thoughtworks.archguard.report.domain.sizing

data class FilterSizing(
    val limit: Long,
    val offset: Long,
    val module: String,
    val className: String,
    val packageName: String,
    val name: String
)
