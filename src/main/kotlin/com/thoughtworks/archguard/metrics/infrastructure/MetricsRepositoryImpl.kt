package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.metrics.domain.coupling.MetricsRepository
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetricsLegacy
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class MetricsRepositoryImpl(
        val moduleMetricsDao: ModuleMetricsDao,
        val packageMetricsDao: PackageMetricsDao,
        val classMetricsDao: ClassMetricsDao,
        val classCouplingDtoDaoForInsert: ClassCouplingDtoDaoForInsert,
        val classCouplingDtoDaoForRead: ClassCouplingDtoDaoForRead
) : MetricsRepository {

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    override fun insert(moduleMetrics: List<ModuleMetricsLegacy>) {
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

    @Transaction
    override fun insertAllClassCouplings(classCouplings: List<ClassCoupling>) {
        classCouplings.forEach { classCouplingDtoDaoForInsert.insert(ClassCouplingDtoForWriteDb.fromClassCoupling(it)) }
    }

    override fun getClassCoupling(jClassVO: JClassVO): ClassCoupling? {
        val classCoupling = classCouplingDtoDaoForRead.findClassCoupling(jClassVO.id!!)
        if (classCoupling != null) {
            return classCoupling.toClassCoupling()
        }
        return null
    }

    override fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling> {
        return classCouplingDtoDaoForRead.findClassCouplings(jClassVOs.map { it.id!! }).map { it.toClassCoupling() }
    }

    @Transaction(TransactionIsolationLevel.REPEATABLE_READ)
    override fun findAllMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy> {
        return moduleNames.stream()
                .map { moduleMetricsDao.findModuleMetricsByModuleName(it) }
                .filter { it != null }
                .peek { moduleMetrics ->
                    moduleMetrics.packageMetrics = packageMetricsDao.findPackageMetricsByModuleId(moduleMetrics.id!!)
                    moduleMetrics.packageMetrics.map {
                        it.classMetrics = classMetricsDao.findClassMetricsByPackageId(it.id!!)
                    }
                }
                .collect(Collectors.toList())
    }

    override fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy> {
        return moduleNames.stream()
                .map { moduleMetricsDao.findModuleMetricsByModuleName(it) }
                .filter { it != null }
                .collect(Collectors.toList())
    }

}