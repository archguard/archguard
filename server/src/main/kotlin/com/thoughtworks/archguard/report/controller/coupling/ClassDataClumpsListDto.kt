package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.coupling.dataclumps.ClassDataClump

data class ClassDataClumpsListDto(
    val data: List<ClassDataClump>,
    val count: Long,
    val currentPageNumber: Long,
    val threshold: Int
)
