package com.thoughtworks.archguard.report.domain.circulardependency

data class CircularDependencyListDto(val circularDependencyList: List<String>, val circularDependencyCount: Long, val currentPageNumber: Long)
