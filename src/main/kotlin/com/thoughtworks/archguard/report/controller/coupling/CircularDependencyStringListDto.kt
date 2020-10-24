package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependency

data class CircularDependencyStringListDto(val data: List<String>, val count: Long, val currentPageNumber: Long, val threshold: Int)

data class CircularDependencyListDto<T>(val data: List<CircularDependency<T>>, val count: Long, val currentPageNumber: Long, val threshold: Int)
