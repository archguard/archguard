package com.thoughtworks.archguard.archguardpackage.domain.repository

import com.thoughtworks.archguard.archguardpackage.domain.dto.PackageDependenceDTO

interface PackageRepository {
    fun getPackageDependence(): List<PackageDependenceDTO>
}
