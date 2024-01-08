package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.metric.PackageMetric

interface PackageMetricRepository {
    fun insertOrUpdatePackageMetric(systemId: Long, packageMetrics: List<PackageMetric>)
}
