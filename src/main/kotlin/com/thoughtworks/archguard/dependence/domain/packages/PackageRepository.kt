package com.thoughtworks.archguard.dependence.domain.packages

import com.thoughtworks.archguard.dependence.infrastructure.packages.PackageDependenceDTO


interface PackageRepository {
    fun getPackageDependence(): List<PackageDependenceDTO>
}
