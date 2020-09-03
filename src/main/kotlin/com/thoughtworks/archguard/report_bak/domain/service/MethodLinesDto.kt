package com.thoughtworks.archguard.report_bak.domain.service

import com.thoughtworks.archguard.report_bak.domain.model.MethodLine

data class MethodLinesDto(val methodLines: List<MethodLine>, val count: Long, val currentPageNumber: Long)