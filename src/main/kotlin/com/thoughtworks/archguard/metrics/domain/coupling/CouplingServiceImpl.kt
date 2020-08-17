package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.stereotype.Service

@Service
class CouplingServiceImpl(val jClassRepository: JClassRepository, val logicModuleRepository: LogicModuleRepository, val dependencyService: DependencyService) : CouplingService {
    override fun persistAllClassCouplingResults() {
        TODO("Not yet implemented")
    }

    override fun getAllClassCouplingResults(): List<ClassCoupling> {
        val classes = jClassRepository.getJClassesHasModules()
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        return classes.map { getClassCouplingWithData(it.toVO(), classDependency, logicModules) }
    }

    override fun calculateClassCoupling(jClassVO: JClassVO): ClassCoupling {
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        return getClassCouplingWithData(jClassVO, classDependency, logicModules)
    }

    override fun calculatePackageCoupling(packageVO: PackageVO): PackageCoupling {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        return calculatePackageCouplingWithData(classes, packageVO, classDependency, logicModules)
    }

    override fun calculateModuleCoupling(logicModule: LogicModule): ModuleCoupling {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()

        return calculateModuleCouplingWithData(classes, logicModules, logicModule, classDependency)
    }

    fun getClassCouplingWithData(clazz: JClassVO, dependency: List<Dependency<JClassVO>>,
                                 modules: List<LogicModule>): ClassCoupling {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassCoupling(clazz, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun calculatePackageCouplingWithData(classes: List<JClass>, packageVO: PackageVO, classDependency: List<Dependency<JClassVO>>, logicModules: List<LogicModule>): PackageCoupling {
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val classCouplings = classesBelongToPackage.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return PackageCoupling.of(packageVO, classCouplings)
    }

    private fun calculateModuleCouplingWithData(classes: List<JClass>, logicModules: List<LogicModule>, logicModule: LogicModule, classDependency: List<Dependency<JClassVO>>): ModuleCoupling {
        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return ModuleCoupling.of(logicModule, classCouplings)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClassVO>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules).isNotEmpty()
    }
}