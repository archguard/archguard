package com.thoughtworks.archguard.metrics.domain.coupling

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
    override fun calculateClassCoupling(jClassVO: JClassVO): ClassCoupling {
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        return getClassCoupling(jClassVO, classDependency, logicModules)
    }

    override fun calculatePackageCoupling(packageVO: PackageVO): PackageCoupling {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val classCouplings = classesBelongToPackage.map { it.toVO() }.map { getClassCoupling(it, classDependency, logicModules) }
        return PackageCoupling.of(packageVO, classCouplings)
    }

    override fun calculateModuleCoupling(logicModule: LogicModule): ModuleCoupling {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classDependency = dependencyService.getAllClassDependencies()

        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCoupling(it, classDependency, logicModules) }
        return ModuleCoupling.of(logicModule, classCouplings)
    }

    fun getClassCoupling(clazz: JClassVO, dependency: List<Dependency<JClassVO>>,
                         modules: List<LogicModule>): ClassCoupling {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassCoupling(clazz, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClassVO>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules).isNotEmpty()
    }
}