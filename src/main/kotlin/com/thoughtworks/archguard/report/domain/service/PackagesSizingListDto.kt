package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.model.PackageSizing


data class PackagesSizingListDto(val data: List<PackageSizing>, val count: Long, val currentPageNumber: Long)
