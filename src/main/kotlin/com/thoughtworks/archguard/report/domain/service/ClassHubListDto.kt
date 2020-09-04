package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.ClassHub

data class ClassHubListDto(val data: List<ClassHub>, val count: Long, val currentPageNumber: Long)