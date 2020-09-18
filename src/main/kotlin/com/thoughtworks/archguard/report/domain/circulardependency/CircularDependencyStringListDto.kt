package com.thoughtworks.archguard.report.domain.circulardependency

data class CircularDependencyStringListDto(val data: List<String>, val count: Long, val currentPageNumber: Long)