package com.thoughtworks.archguard.report.domain.coupling.circulardependency

data class CircularDependencyStringListDto(val data: List<String>, val count: Long, val currentPageNumber: Long)

data class CircularDependencyListDto<T>(val data: List<CircularDependency<T>>, val count: Long, val currentPageNumber: Long)
