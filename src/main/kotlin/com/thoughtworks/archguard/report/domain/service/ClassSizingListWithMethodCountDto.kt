package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.ClassSizingWithMethodCount

data class ClassSizingListWithMethodCountDto(val data: List<ClassSizingWithMethodCount>, val count: Long, val currentPageNumber: Long)
