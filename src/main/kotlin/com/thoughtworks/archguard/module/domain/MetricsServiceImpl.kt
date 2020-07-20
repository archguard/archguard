package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.metrics.ClassMetrics
import com.thoughtworks.archguard.module.domain.metrics.ModuleMetrics
import com.thoughtworks.archguard.module.domain.metrics.PackageMetrics
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MetricsServiceImpl(
        @Autowired val metricsRepository: MetricsRepository,
        @Autowired val logicModuleRepository: LogicModuleRepository,
        @Autowired val dependencyService: DependencyService
) : MetricsService {

    override fun calculateCoupling() {
        val modules = logicModuleRepository.getAll()
        val dependencies = dependencyService.getAll()
        val classDependency = dependencies.map { Dependency(JClassVO(it.caller.className, it.caller.moduleName), JClassVO(it.callee.className, it.callee.moduleName)) }

        val classMetrics = getClassMetrics(classDependency, modules)
        val moduleMetrics = groupPackageMetrics(groupToPackage(classMetrics), modules)
        metricsRepository.insert(moduleMetrics)
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
        }.toList().asSequence()
                  .flatMap { it.asSequence() }
                  .groupBy({it.key}, {it.value})
                  .map { ModuleMetrics.of(it.key, it.value) }
    }

}
