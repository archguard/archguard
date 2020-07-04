package com.thoughtworks.archguard.packages.domain

import com.thoughtworks.archguard.packages.infrastructure.PackageDependenceDTO


interface PackageRepository {
    fun getPackageDependenceByModule(module: String): List<PackageDependenceDTO>
}
