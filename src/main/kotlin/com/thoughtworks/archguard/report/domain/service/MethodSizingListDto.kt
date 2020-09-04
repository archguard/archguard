package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.MethodSizing


data class MethodSizingListDto(val data: List<MethodSizing>, val count: Long, val currentPageNumber: Long)