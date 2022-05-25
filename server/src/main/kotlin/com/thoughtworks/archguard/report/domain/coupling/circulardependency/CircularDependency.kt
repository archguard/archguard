package com.thoughtworks.archguard.report.domain.coupling.circulardependency

data class CircularDependency<T>(val circularDependency: List<T>)
