package com.thoughtworks.archguard.code.packages.domain

import com.thoughtworks.archguard.code.packages.infrastructure.PackageDependenceDTO

interface PackageRepository {
    fun getPackageDependenceByModuleFull(systemId: Long, module: String): List<PackageDependenceDTO>
    fun getPackageDependenceByClass(systemId: Long, module: String): List<PackageDependenceDTO>
}
