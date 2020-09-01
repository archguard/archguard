package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.clazz.domain.JClass
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
import com.thoughtworks.archguard.metrics.domain.dit.DitService
import com.thoughtworks.archguard.metrics.domain.lcom4.LCOM4Service
import com.thoughtworks.archguard.metrics.domain.noc.NocService
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.graph.GraphStore
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
        val couplingService: CouplingService,
        val lcom4Service: LCOM4Service,
        val ditService: DitService
) : MetricsService {
    private val log = LoggerFactory.getLogger(MetricsServiceImpl::class.java)

    override fun calculateCouplingLegacy(systemId: Long) {
        val modules = logicModuleRepository.getAllBysystemId(systemId)
        val classDependency = dependencyService.getAllClassDependencies(systemId)

        val classMetrics = getClassMetrics(classDependency, modules)
        val moduleMetrics = groupPackageMetrics(groupToPackage(classMetrics), modules)
        log.info("calculate coupling Done! Metrics: $moduleMetrics")
        metricsRepository.insert(moduleMetrics)
    }

    override fun getAllMetricsLegacy(systemId: Long): List<ModuleMetricsLegacy> {
        val modules = logicModuleRepository.getAllByShowStatus(systemId, true)
        val moduleNames = modules.map { it.name }.toList()
        var metrics = metricsRepository.findAllMetrics(moduleNames)
        if (metrics.isEmpty()) {
            calculateCouplingLegacy(systemId)
            metrics = metricsRepository.findAllMetrics(moduleNames)
        }
        return metrics
    }

    override fun getClassAbstractMetric(systemId: Long, jClassVO: JClassVO): ClassAbstractRatio {
        val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Not Found JClass with Module ${jClassVO.module} and ClassName ${jClassVO.name}")
        val methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        jClass.methods = methods
        return ClassAbstractRatio.fromJClass(jClass)
    }

    override fun getPackageAbstractMetric(systemId: Long, packageVO: PackageVO): PackageAbstractRatio {
        return abstractAnalysisService.calculatePackageAbstractRatio(systemId, packageVO)
    }

    override fun getClassNoc(systemId: Long, jClassVO: JClassVO): Int {
        val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Not Found JClass with Module ${jClassVO.module} and ClassName ${jClassVO.name}")
        return nocService.getNoc(systemId, jClass)
    }

    override fun getClassAbc(systemId: Long, jClassVO: JClassVO): Int {
        val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFoundException("Cannot find class by name: $jClassVO.name module: $jClassVO.module")
        jClass.methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        return abcService.calculateAbc(jClass)
    }

    override fun calculateAllNoc(systemId: Long): List<ClassNoc> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        val classNocList = mutableListOf<ClassNoc>()
        jClasses.forEach { classNocList.add(ClassNoc(it.toVO(), nocService.getNoc(systemId, it))) }
        return classNocList
    }

    override fun calculateAllDit(systemId: Long): List<ClassDit> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        val classDitList = mutableListOf<ClassDit>()
        jClasses.forEach { classDitList.add(ClassDit(it.toVO(), ditService.getDepthOfInheritance(systemId, it))) }
        return classDitList
    }

    override fun calculateAllAbc(systemId: Long): List<ClassAbc> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        jClasses.forEach { it.methods = jMethodRepository.findMethodsByModuleAndClass(systemId, it.module, it.name) }

        val classAbcList = mutableListOf<ClassAbc>()
        jClasses.forEach { classAbcList.add(ClassAbc(it.toVO(), abcService.calculateAbc(it))) }
        return classAbcList
    }

    override fun calculateAllLCOM4(systemId: Long): List<ClassLCOM4> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        jClasses.forEach { prepareJClassBasicDataForLCOM4(systemId, it) }

        val classLCOM4List = mutableListOf<ClassLCOM4>()
        jClasses.forEach { classLCOM4List.add(ClassLCOM4(it.toVO(), lcom4Service.getLCOM4Graph(it).getConnectivityCount())) }
        return classLCOM4List
    }

    override fun calculateAllModuleDfms(systemId: Long): List<ModuleDfms> {
        val logicModules = logicModuleRepository.getAllBysystemId(systemId)
        return logicModules.map { ModuleDfms.of(it, couplingService.calculateModuleCoupling(systemId, it), getModuleAbstractMetric(systemId, it.name)) }
    }

    override fun calculateAllClassDfms(systemId: Long): List<ClassDfms> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        return jClasses.map { getClassDfms(systemId, it.toVO()) }
    }

    override fun getClassDfms(systemId: Long, jClassVO: JClassVO): ClassDfms {
        return ClassDfms.of(jClassVO, couplingService.calculateClassCoupling(systemId, jClassVO), getClassAbstractMetric(systemId, jClassVO))
    }

    override fun getPackageDfms(systemId: Long, packageVO: PackageVO): PackageDfms {
        return PackageDfms.of(packageVO, couplingService.calculatePackageCoupling(systemId, packageVO), getPackageAbstractMetric(systemId, packageVO))
    }

    override fun getModuleDfms(systemId: Long, moduleName: String): ModuleDfms {
        val logicModule = logicModuleRepository.get(systemId, moduleName)
        return ModuleDfms.of(logicModule, couplingService.calculateModuleCoupling(systemId, logicModule), getModuleAbstractMetric(systemId, moduleName))
    }

    override fun getModuleAbstractMetric(systemId: Long, moduleName: String): ModuleAbstractRatio {
        val logicModule = logicModuleRepository.get(systemId, moduleName)
        return abstractAnalysisService.calculateModuleAbstractRatio(systemId, logicModule)
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

    override fun getClassLCOM4(systemId: Long, jClassVO: JClassVO): GraphStore {
        val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("""Cannot find class with module: ${jClassVO.module} name: ${jClassVO.name}""")
        prepareJClassBasicDataForLCOM4(systemId, jClass)
        return lcom4Service.getLCOM4Graph(jClass)
    }

    private fun prepareJClassBasicDataForLCOM4(systemId: Long, jClass: JClass) {
        jClass.fields = jClassRepository.findFields(jClass.id)
        val methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        methods.forEach { it.fields = jMethodRepository.findMethodFields(it.id) }
        methods.forEach { it.callees = jMethodRepository.findMethodCallees(it.id) }
        jClass.methods = methods
    }

    override fun getClassDit(systemId: Long, jClassVO: JClassVO): Int {
        val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("""Cannot find class with module: ${jClassVO.module} name: ${jClassVO.name}""")
        return ditService.getDepthOfInheritance(systemId, jClass)
    }
}
