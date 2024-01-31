package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.sizing.FilterSizing
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated

@Serializable
@Validated
data class FilterSizingDto(
    @Transient
    @NotNull
    val currentPageNumber: Long,
    @Transient
    @NotNull
    val numberPerPage: Long,
    val module: String? = null,
    val className: String? = null,
    val packageName: String? = null,
    val name: String? = null,
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
