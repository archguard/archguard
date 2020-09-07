package com.thoughtworks.archguard.report.domain.sizing


data class MethodSizingListDto(val data: List<MethodSizing>, val count: Long, val currentPageNumber: Long)