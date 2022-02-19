package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.PackageMetric
import com.thoughtworks.archgard.scanner2.domain.repository.PackageMetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class PackageMetricRepositoryImpl(val packageMetricsDao: PackageMetricsDao) : PackageMetricRepository {
    private val log = LoggerFactory.getLogger(ModuleMetricRepositoryImpl::class.java)

    override fun insertOrUpdatePackageMetric(systemId: Long, packageMetrics: List<PackageMetric>) {
        packageMetricsDao.deleteBy(systemId)
        log.info("Delete system package metric old data with id: {}", systemId)
        if (packageMetrics.isEmpty()) {
            log.warn("Insert system package metric new data with id is empty!: {}", systemId)
            return
        }
        packageMetricsDao.insert(packageMetrics)
        log.info("Insert system package metric new data with id: {}", systemId)
    }
}