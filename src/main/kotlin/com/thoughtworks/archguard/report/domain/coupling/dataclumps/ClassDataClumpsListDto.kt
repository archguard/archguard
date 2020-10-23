package com.thoughtworks.archguard.report.domain.coupling.dataclumps

data class ClassDataClumpsListDto(val data: List<ClassDataClump>, val count: Long, val currentPageNumber: Long)