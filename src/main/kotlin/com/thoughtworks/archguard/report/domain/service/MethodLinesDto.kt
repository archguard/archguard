package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.MethodLine


data class MethodLinesDto(val data: List<MethodLine>, val count: Long, val currentPageNumber: Long)