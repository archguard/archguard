package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.ModuleSizing


data class ModulesSizingListDto(val data: List<ModuleSizing>, val count: Long, val currentPageNumber: Long)
