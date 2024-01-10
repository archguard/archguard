package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.sizing.FilterSizing
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated

@Serializable
@Validated
data class FilterSizingDto(
    @NotNull val currentPageNumber: Long,
    @NotNull val numberPerPage: Long,
    val module: String?,
    val className: String?,
    val packageName: String?,
    val name: String?
) {
    fun getLimit(): Long {
        return numberPerPage
    }

    fun getOffset(): Long {
        return (currentPageNumber - 1) * getLimit()
    }

    fun toFilterSizing(): FilterSizing {
        return FilterSizing(
            getLimit(),
            getOffset(),
            module ?: "",
            className ?: "",
            packageName ?: "",
            name ?: ""
        )
    }
}
