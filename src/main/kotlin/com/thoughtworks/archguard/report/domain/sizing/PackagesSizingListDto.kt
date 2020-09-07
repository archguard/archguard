package com.thoughtworks.archguard.report.domain.sizing


data class PackagesSizingListDto(val data: List<PackageSizing>, val count: Long, val currentPageNumber: Long)
