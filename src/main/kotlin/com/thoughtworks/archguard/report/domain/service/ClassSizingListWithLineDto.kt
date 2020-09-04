package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.ClassSizingWithLine

data class ClassSizingListWithLineDto(val data: List<ClassSizingWithLine>, val count: Long, val currentPageNumber: Long)
