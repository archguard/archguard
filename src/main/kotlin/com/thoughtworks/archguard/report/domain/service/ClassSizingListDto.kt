package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.ClassSizing

data class ClassSizingListDto(val data: List<ClassSizing>, val count: Long, val currentPageNumber: Long)
