package com.thoughtworks.archguard.packages.domain

import com.thoughtworks.archguard.packages.infrastructure.PackageDependenceDTO


interface PackageRepository {
    fun getPackageDependenceByModuleFull(systemId: Long, module: String): List<PackageDependenceDTO>
    fun getPackageDependenceByClass(systemId: Long, module: String): List<PackageDependenceDTO>
}
