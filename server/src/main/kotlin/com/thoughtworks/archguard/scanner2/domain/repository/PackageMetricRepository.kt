package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.PackageMetric

interface PackageMetricRepository {
    fun insertOrUpdatePackageMetric(systemId: Long, packageMetrics: List<PackageMetric>)
}
