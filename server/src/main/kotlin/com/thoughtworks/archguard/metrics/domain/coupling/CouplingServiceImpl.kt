package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.code.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.code.module.domain.getModule
import org.archguard.arch.LogicModule
import org.archguard.model.vos.PackageVO
import com.thoughtworks.archguard.metrics.domain.MetricsRepository
import org.archguard.model.Dependency
import org.archguard.model.vos.JClassVO
import org.springframework.stereotype.Service

@Service
class CouplingServiceImpl(
    val jClassRepository: JClassRepository,
    val logicModuleRepository: LogicModuleRepository,
    val dependencyService: DependencyService,
    val metricsRepository: MetricsRepository
) : CouplingService {
    override fun persistAllClassCouplingResults(systemId: Long) {
        val classCouplingResults = getAllClassCouplingResults(systemId)
        metricsRepository.insertAllClassCouplings(systemId, classCouplingResults)
    }

    override fun getAllClassCouplingResults(systemId: Long): List<ClassCoupling> {
        val classes = jClassRepository.getJClassesHasModules(systemId)
        val logicModules = logicModuleRepository.getAllBySystemId(systemId)
        val classDependency = dependencyService.getAllClassDependencies(systemId)
        return classes.map { getClassCouplingWithData(it.toVO(), classDependency, logicModules) }
    }

    override fun calculateClassCoupling(systemId: Long, jClassVO: JClassVO): ClassCoupling {
        val jClassVOHasId = if (jClassVO.id != null) {
            jClassVO
        } else {
            val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Cannot found class with module: ${jClassVO.module} and name: ${jClassVO.name}")
            jClass.toVO()
        }
        val classCoupling = metricsRepository.getClassCoupling(jClassVOHasId)
        if (classCoupling != null) {
            return classCoupling
        }

        val logicModules = logicModuleRepository.getAllBySystemId(systemId)
        val classDependency = dependencyService.getAllClassDependencies(systemId)
        return getClassCouplingWithData(jClassVO, classDependency, logicModules)
    }

    override fun calculateClassCouplings(systemId: Long, jClassVOs: List<JClassVO>): List<ClassCoupling> {
        return jClassVOs.map { calculateClassCoupling(systemId, it) }
    }

    override fun calculatePackageCouplings(systemId: Long, packageVOs: List<PackageVO>): List<PackageCoupling> {
        return packageVOs.map { calculatePackageCoupling(systemId, it) }
    }

    override fun calculatePackageCoupling(systemId: Long, packageVO: PackageVO): PackageCoupling {
        val classes = jClassRepository.getAllBySystemId(systemId)
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToPackage.map { it.toVO() })
        if (classCouplingsCached.isNotEmpty()) {
            return PackageCoupling.of(packageVO, classCouplingsCached)
        }

        val logicModules = logicModuleRepository.getAllBySystemId(systemId)
        val classDependency = dependencyService.getAllClassDependencies(systemId)
        val classCouplings = classesBelongToPackage.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return PackageCoupling.of(packageVO, classCouplings)
    }

    override fun calculatePackageDirectClassCouplings(systemId: Long, packageVO: PackageVO): List<ClassCoupling> {
        val classes = jClassRepository.getAllBySystemId(systemId)
        val classesDirectBelongToPackage = classes.filter { packageVO.directContainClass(it.toVO()) }
        return calculateClassCouplings(systemId, classesDirectBelongToPackage.map { it.toVO() })
    }

    override fun calculateModuleCoupling(systemId: Long, logicModule: LogicModule): ModuleCoupling {
        val classes = jClassRepository.getAllBySystemId(systemId)
        val logicModules = logicModuleRepository.getAllBySystemId(systemId)
        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToModule.map { it.toVO() })
        if (classCouplingsCached.isNotEmpty()) {
            return ModuleCoupling.of(logicModule, classCouplingsCached)
        }

        val classDependency = dependencyService.getAllClassDependencies(systemId)
        val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return ModuleCoupling.of(logicModule, classCouplings)
    }

//    @Cacheable("allModuleCoupling")
    override fun calculateAllModuleCoupling(systemId: Long): List<ModuleCoupling> {
        val classes = jClassRepository.getAllBySystemId(systemId)
        val logicModules = logicModuleRepository.getAllBySystemId(systemId)
        val moduleCouplings = mutableListOf<ModuleCoupling>()
        val classDependency = mutableListOf<Dependency<JClassVO>>()
        logicModules.parallelStream().forEach { logicModule ->
            val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
            val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToModule.map { it.toVO() })
            if (classCouplingsCached.isNotEmpty()) {
                moduleCouplings.add(ModuleCoupling.of(logicModule, classCouplingsCached))
                return@forEach
            }
            if (classDependency.isEmpty()) {
                classDependency.addAll(dependencyService.getAllClassDependencies(systemId))
            }
            val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
            moduleCouplings.add(ModuleCoupling.of(logicModule, classCouplings))
        }
        return moduleCouplings
    }

    fun getClassCouplingWithData(
        clazz: JClassVO,
        dependency: List<Dependency<JClassVO>>,
        modules: List<LogicModule>
    ): ClassCoupling {
        val innerFanIn = dependency.filter { it.callee == clazz }.count { isInSameModule(modules, it) }
        val innerFanOut = dependency.filter { it.caller == clazz }.count { isInSameModule(modules, it) }
        val outerFanIn = dependency.filter { it.callee == clazz }.count { !isInSameModule(modules, it) }
        val outerFanOut = dependency.filter { it.caller == clazz }.count { !isInSameModule(modules, it) }

        return ClassCoupling(clazz, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClassVO>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules.toSet()).isNotEmpty()
    }
}
