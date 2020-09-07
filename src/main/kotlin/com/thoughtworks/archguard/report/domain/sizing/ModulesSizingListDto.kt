package com.thoughtworks.archguard.report.domain.sizing


data class ModulesSizingListDto(val data: List<ModuleSizing>, val count: Long, val currentPageNumber: Long)
