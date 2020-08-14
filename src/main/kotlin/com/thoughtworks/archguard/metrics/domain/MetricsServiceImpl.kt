package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.metrics.domain.abc.AbcService
import com.thoughtworks.archguard.metrics.domain.abstracts.AbstractAnalysisService
import com.thoughtworks.archguard.metrics.domain.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.metrics.domain.coupling.ClassMetricsLegacy
import com.thoughtworks.archguard.metrics.domain.coupling.CouplingService
import com.thoughtworks.archguard.metrics.domain.coupling.MetricsRepository
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetricsLegacy
import com.thoughtworks.archguard.metrics.domain.coupling.PackageMetricsLegacy
import com.thoughtworks.archguard.metrics.domain.dfms.ClassDfms
import com.thoughtworks.archguard.metrics.domain.dfms.ModuleDfms
import com.thoughtworks.archguard.metrics.domain.dfms.PackageDfms
import com.thoughtworks.archguard.metrics.domain.noc.NocService
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
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
        val abstractAnalysisService: AbstractAnalysisService,
        val jMethodRepository: JMethodRepository,
        val nocService: NocService,
        val abcService: AbcService,
        val couplingService: CouplingService
) : MetricsService {
    private val log = LoggerFactory.getLogger(MetricsServiceImpl::class.java)

    override fun calculateCouplingLegacy() {
        val modules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()

        val classMetrics = getClassMetrics(classDependency, modules)
        val moduleMetrics = groupPackageMetrics(groupToPackage(classMetrics), modules)
        log.info("calculate coupling Done! Metrics: $moduleMetrics")
        metricsRepository.insert(moduleMetrics)
    }

    override fun getAllMetricsLegacy(): List<ModuleMetricsLegacy> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val moduleNames = modules.map { it.name }.toList()
        return metricsRepository.findAllMetrics(moduleNames)
    }

    override fun getModuleMetricsLegacy(): List<ModuleMetricsLegacy> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val moduleNames = modules.map { it.name }.toList()
        return metricsRepository.findModuleMetrics(moduleNames)
    }

    override fun getClassAbstractMetric(jClassVO: JClassVO): ClassAbstractRatio {
        val jClass = jClassRepository.getJClassBy(jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Not Found JClass with Module ${jClassVO.module} and ClassName ${jClassVO.name}")
        val methods = jMethodRepository.findMethodsByModuleAndClass(jClass.module, jClass.name)
        jClass.methods = methods
        return ClassAbstractRatio.fromJClass(jClass)
    }

    override fun getPackageAbstractMetric(packageVO: PackageVO): PackageAbstractRatio {
        return abstractAnalysisService.calculatePackageAbstractRatio(packageVO)
    }

    override fun getClassNoc(jClassVO: JClassVO): Int {
        val jClass = jClassRepository.getJClassBy(jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Not Found JClass with Module ${jClassVO.module} and ClassName ${jClassVO.name}")
        return nocService.getNoc(jClass)
    }

    override fun getClassAbc(jClassVO: JClassVO): Int {
        val jClass = jClassRepository.getJClassBy(jClassVO.name, jClassVO.module)
                ?: throw ClassNotFoundException("Cannot find class by name: $jClassVO.name module: $jClassVO.module")
        jClass.methods = jMethodRepository.findMethodsByModuleAndClass(jClass.module, jClass.name)
        return abcService.calculateAbc(jClass)
    }

    override fun getClassDfms(jClassVO: JClassVO): ClassDfms {
        return ClassDfms.of(jClassVO, couplingService.calculateClassCoupling(jClassVO), getClassAbstractMetric(jClassVO))
    }

    override fun getPackageDfms(packageVO: PackageVO): PackageDfms {
        return PackageDfms.of(packageVO, couplingService.calculatePackageCoupling(packageVO), getPackageAbstractMetric(packageVO))
    }

    override fun getModuleDfms(moduleName: String): ModuleDfms {
        val logicModule = logicModuleRepository.get(moduleName)
        return ModuleDfms.of(logicModule, couplingService.calculateModuleCoupling(logicModule), getModuleAbstractMetric(moduleName))
    }

    override fun getModuleAbstractMetric(moduleName: String): ModuleAbstractRatio {
        val logicModule = logicModuleRepository.get(moduleName)
        return abstractAnalysisService.calculateModuleAbstractRatio(logicModule)
    }

    private fun getClassMetrics(dependency: List<Dependency<JClassVO>>,
                                modules: List<LogicModule>): List<ClassMetricsLegacy> {
        return dependency.flatMap { listOf(it.callee, it.caller) }.distinct()
                .map { getClassCouplingLegacy(it, dependency, modules) }
    }

    fun getClassCouplingLegacy(clazz: JClassVO, dependency: List<Dependency<JClassVO>>,
                               modules: List<LogicModule>): ClassMetricsLegacy {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassMetricsLegacy.of(clazz.getFullName(), innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClassVO>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules).isNotEmpty()
    }

    fun groupToPackage(classMetrics: List<ClassMetricsLegacy>): List<PackageMetricsLegacy> {
        return classMetrics.groupBy { it.className.substringBeforeLast('.') }
                .map { PackageMetricsLegacy.of(it.key, it.value) }
    }

    fun groupPackageMetrics(packageMetrics: List<PackageMetricsLegacy>,
                            modules: List<LogicModule>): List<ModuleMetricsLegacy> {
        return packageMetrics.map { packages ->
            getModule(modules, SubModule(packages.packageName)).groupBy { it.name }.mapValues { packages }
        }.toList().asSequence().flatMap { it.asSequence() }
                .groupBy({ it.key }, { it.value })
                .map { ModuleMetricsLegacy.of(it.key, it.value) }
    }

}
