package com.thoughtworks.archguard.module.infrastructure.metrics

import com.thoughtworks.archguard.module.domain.MetricsRepository
import com.thoughtworks.archguard.module.domain.metrics.coupling.ModuleMetrics
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class MetricsRepositoryImpl(
        @Autowired val moduleMetricsDao: ModuleMetricsDao,
        @Autowired val packageMetricsDao: PackageMetricsDao,
        @Autowired val classMetricsDao: ClassMetricsDao
) : MetricsRepository {

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
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

    @Transaction(TransactionIsolationLevel.REPEATABLE_READ)
    override fun findAllMetrics(moduleNames: List<String>): List<ModuleMetrics> {
        return moduleNames.stream()
                .map { moduleMetricsDao.findModuleMetricsByModuleName(it)}
                .filter{ it != null }
                .peek{ moduleMetrics ->
                    moduleMetrics.packageMetrics = packageMetricsDao.findPackageMetricsByModuleId(moduleMetrics.id!!)
                    moduleMetrics.packageMetrics.map {
                        it.classMetrics = classMetricsDao.findClassMetricsByPackageId(it.id!!)
                    } }
                .collect(Collectors.toList())
    }

    override fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetrics> {
        return moduleNames.stream()
                .map { moduleMetricsDao.findModuleMetricsByModuleName(it)}
                .filter{ it != null }
                .collect(Collectors.toList())
    }

}