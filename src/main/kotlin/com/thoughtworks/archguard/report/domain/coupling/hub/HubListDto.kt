package com.thoughtworks.archguard.report.domain.coupling.hub

data class ClassHubListDto(val data: List<ClassCoupling>, val count: Long, val currentPageNumber: Long)
data class MethodHubListDto(val data: List<MethodCoupling>, val count: Long, val currentPageNumber: Long)
data class PackageHubListDto(val data: List<PackageCoupling>, val count: Long, val currentPageNumber: Long)
data class ModuleHubListDto(val data: List<ModuleCoupling>, val count: Long, val currentPageNumber: Long)