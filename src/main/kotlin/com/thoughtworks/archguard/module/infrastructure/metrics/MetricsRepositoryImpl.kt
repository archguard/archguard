package com.thoughtworks.archguard.module.infrastructure.metrics

import com.thoughtworks.archguard.module.domain.MetricsRepository
import com.thoughtworks.archguard.module.domain.metrics.ModuleMetrics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class MetricsRepositoryImpl(
        @Autowired val moduleMetricsDao: ModuleMetricsDao,
        @Autowired val packageMetricsDao: PackageMetricsDao,
        @Autowired val classMetricsDao: ClassMetricsDao
) : MetricsRepository {

    override fun insert(moduleMetrics: List<ModuleMetrics>) {
        classMetricsDao.truncate()
        packageMetricsDao.truncate()
        moduleMetricsDao.truncate()

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

    override fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetrics> {
        return moduleNames.stream()
                .map { moduleMetricsDao.findModuleMetricsByModuleName(it)}
                        //.orElseThrow { RuntimeException("module not found with $it") } }
                .filter{ it != null }
                .peek{ moduleMetrics ->
                    moduleMetrics.packageMetrics = packageMetricsDao.findPackageMetricsByModuleId(moduleMetrics.id!!)
                    moduleMetrics.packageMetrics.map {
                        it.classMetrics = classMetricsDao.findClassMetricsByPackageId(it.id!!)
                    } }
                .collect(Collectors.toList())
    }
}