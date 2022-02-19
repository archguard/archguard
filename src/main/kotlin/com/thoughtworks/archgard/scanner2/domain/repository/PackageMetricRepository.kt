package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.PackageMetric

interface PackageMetricRepository {
    fun insertOrUpdatePackageMetric(systemId: Long, packageMetrics: List<PackageMetric>)

}
