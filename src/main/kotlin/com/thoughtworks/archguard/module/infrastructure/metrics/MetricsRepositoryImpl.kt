package com.thoughtworks.archguard.module.infrastructure.metrics

import com.thoughtworks.archguard.module.domain.MetricsRepository
import com.thoughtworks.archguard.module.domain.metrics.ModuleMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class MetricsRepositoryImpl(
        @Autowired val moduleMetricsDao: ModuleMetricsDao,
        @Autowired val packageMetricsDao: PackageMetricsDao,
        @Autowired val classMetricsDao: ClassMetricsDao
) : MetricsRepository {

    override fun insert(moduleMetrics: List<ModuleMetrics>) {
        moduleMetrics.map { modules ->
            val moduleId = moduleMetricsDao.insert(modules)
            modules.packageMetrics.map { packages ->
                packages.moduleId = moduleId
                val packageId = packageMetricsDao.insert(packages)
                packages.classMetrics.map {
                    it.packageId = packageId
                    classMetricsDao.insert(it)
                }
            }
        }
    }
}