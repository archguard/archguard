package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.coupling.ClassMetrics
import com.thoughtworks.archguard.module.domain.metrics.coupling.ModuleMetrics
import com.thoughtworks.archguard.module.domain.metrics.coupling.PackageMetrics
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import com.thoughtworks.archguard.module.domain.model.SubModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MetricsServiceImpl(
        val metricsRepository: MetricsRepository,
        val logicModuleRepository: LogicModuleRepository,
        val dependencyService: DependencyService,
        val jClassRepository: JClassRepository,
        val abstractAnalysisService: AbstractAnalysisService
) : MetricsService {
    private val log = LoggerFactory.getLogger(MetricsServiceImpl::class.java)

    override fun calculateCoupling() {
        val modules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()

        val classMetrics = getClassMetrics(classDependency, modules)
        val moduleMetrics = groupPackageMetrics(groupToPackage(classMetrics), modules)
        log.info("calculate coupling Done! Metrics: $moduleMetrics")
        metricsRepository.insert(moduleMetrics)
    }

    override fun getAllMetrics(): List<ModuleMetrics> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val moduleNames = modules.map { it.name }.toList()
        return metricsRepository.findAllMetrics(moduleNames)
    }

    override fun getModuleMetrics(): List<ModuleMetrics> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val moduleNames = modules.map { it.name }.toList()
        return metricsRepository.findModuleMetrics(moduleNames)
    }

    override fun getClassAbstractMetric(jClassVO: JClassVO): ClassAbstractRatio {
        val jClass = jClassRepository.getJClassBy(jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Not Found JClass with Module ${jClassVO.module} and ClassName ${jClassVO.name}")
        return ClassAbstractRatio.fromJClass(jClass)
    }

    override fun getPackageAbstractMetric(packageVO: PackageVO): PackageAbstractRatio {
        return abstractAnalysisService.calculatePackageAbstractRatio(packageVO)
    }

    override fun getModuleAbstractMetric(moduleName: String): ModuleAbstractRatio {
        val logicModule = logicModuleRepository.get(moduleName)
        return abstractAnalysisService.calculateModuleAbstractRatio(logicModule)
    }

    private fun getClassMetrics(dependency: List<Dependency<JClassVO>>,
                                modules: List<LogicModule>): List<ClassMetrics> {
        return dependency.flatMap { listOf(it.callee, it.caller) }.distinct()
                .map { getClassCoupling(it, dependency, modules) }
    }

    fun getClassCoupling(clazz: JClassVO, dependency: List<Dependency<JClassVO>>,
                         modules: List<LogicModule>): ClassMetrics {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassMetrics.of(clazz.getFullName(), innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClassVO>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules).isNotEmpty()
    }

    fun groupToPackage(classMetrics: List<ClassMetrics>): List<PackageMetrics> {
        return classMetrics.groupBy { it.className.substringBeforeLast('.') }
                .map { PackageMetrics.of(it.key, it.value) }
    }

    fun groupPackageMetrics(packageMetrics: List<PackageMetrics>,
                            modules: List<LogicModule>): List<ModuleMetrics> {
        return packageMetrics.map { packages ->
            getModule(modules, SubModule(packages.packageName)).groupBy { it.name }.mapValues { packages }
        }.toList().asSequence().flatMap { it.asSequence() }
                .groupBy({ it.key }, { it.value })
                .map { ModuleMetrics.of(it.key, it.value) }
    }

}
