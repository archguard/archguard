package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.MethodCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCoupling

data class ClassHubListDto(val data: List<ClassCoupling>, val count: Long, val currentPageNumber: Long, val threshold: Int)
data class MethodHubListDto(val data: List<MethodCoupling>, val count: Long, val currentPageNumber: Long, val threshold: Int)
data class PackageHubListDto(val data: List<PackageCoupling>, val count: Long, val currentPageNumber: Long, val threshold: Int)
data class ModuleHubListDto(val data: List<ModuleCoupling>, val count: Long, val currentPageNumber: Long, val threshold: Int)
