package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.MethodLine

data class MethodLinesDto(val methodLines: List<MethodLine>, val count: Long, val currentPageNumber: Long)