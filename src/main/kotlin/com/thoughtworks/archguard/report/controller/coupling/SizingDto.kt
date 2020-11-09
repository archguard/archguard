package com.thoughtworks.archguard.report.controller.coupling

import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated


@Validated data class SizingMethodRequestDto(@NotNull val currentPageNumber: Long,
                                                 @NotNull val numberPerPage: Long,
                                                 val module: String,
                                                 val className: String,
                                                 val packageName: String,
                                                 val name: String) {
    fun getLimit(): Long {
        return numberPerPage
    }

    fun getOffset(): Long {
        return (currentPageNumber - 1) * getLimit()
    }
}
