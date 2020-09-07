package com.thoughtworks.archguard.report.domain.dataclumps

data class ClassDataClumpsListDto(val data: List<ClassDataClump>, val count: Long, val currentPageNumber: Long)