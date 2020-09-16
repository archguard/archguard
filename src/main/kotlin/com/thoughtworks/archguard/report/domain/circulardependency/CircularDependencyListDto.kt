package com.thoughtworks.archguard.report.domain.circulardependency

data class CircularDependencyListDto(val data: List<String>, val count: Long, val currentPageNumber: Long)
