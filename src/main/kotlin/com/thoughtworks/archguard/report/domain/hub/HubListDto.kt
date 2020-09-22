package com.thoughtworks.archguard.report.domain.hub

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.MethodCoupling
import com.thoughtworks.archguard.report.domain.coupling.ModuleCoupling
import com.thoughtworks.archguard.report.domain.coupling.PackageCoupling

data class ClassHubListDto(val data: List<ClassCoupling>, val count: Long, val currentPageNumber: Long)
data class MethodHubListDto(val data: List<MethodCoupling>, val count: Long, val currentPageNumber: Long)
data class PackageHubListDto(val data: List<PackageCoupling>, val count: Long, val currentPageNumber: Long)
data class ModuleHubListDto(val data: List<ModuleCoupling>, val count: Long, val currentPageNumber: Long)