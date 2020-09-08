package com.thoughtworks.archguard.report.domain.hub

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling

data class ClassHubListDto(val data: List<ClassCoupling>, val count: Long, val currentPageNumber: Long)