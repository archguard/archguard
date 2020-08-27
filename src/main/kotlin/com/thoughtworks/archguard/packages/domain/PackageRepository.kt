package com.thoughtworks.archguard.packages.domain

import com.thoughtworks.archguard.packages.infrastructure.PackageDependenceDTO


interface PackageRepository {
    fun getPackageDependenceByModule(projectId: Long, module: String): List<PackageDependenceDTO>
}
