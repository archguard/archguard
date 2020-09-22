package com.thoughtworks.archguard.report.domain.hub

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.MethodCoupling

data class ClassHubListDto(val data: List<ClassCoupling>, val count: Long, val currentPageNumber: Long)
data class MethodHubListDto(val data: List<MethodCoupling>, val count: Long, val currentPageNumber: Long)