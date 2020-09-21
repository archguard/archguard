package com.thoughtworks.archguard.report.domain.circulardependency

data class CircularDependency<T>(val circularDependency: List<T>)
