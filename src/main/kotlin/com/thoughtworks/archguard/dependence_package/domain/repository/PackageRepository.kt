package com.thoughtworks.archguard.dependence_package.domain.repository

import com.thoughtworks.archguard.dependence_package.domain.dto.PackageDependenceDTO


interface PackageRepository {
    fun getPackageDependence(): List<PackageDependenceDTO>
}
