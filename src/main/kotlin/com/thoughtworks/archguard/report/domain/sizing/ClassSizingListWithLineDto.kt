package com.thoughtworks.archguard.report.domain.sizing

data class ClassSizingListWithLineDto(val data: List<ClassSizingWithLine>, val count: Long, val currentPageNumber: Long)
