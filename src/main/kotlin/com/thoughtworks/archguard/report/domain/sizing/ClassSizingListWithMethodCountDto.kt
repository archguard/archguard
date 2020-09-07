package com.thoughtworks.archguard.report.domain.sizing

data class ClassSizingListWithMethodCountDto(val data: List<ClassSizingWithMethodCount>, val count: Long, val currentPageNumber: Long)
