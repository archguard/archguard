package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.infrastructure.TestSmellPO

data class MethodInfoListDTO(val data: List<MethodInfo>, val count: Long, val currentPageNumber: Long)

data class IssueListDTO(val data: List<TestSmellPO>, val count: Long, val currentPageNumber: Long)
