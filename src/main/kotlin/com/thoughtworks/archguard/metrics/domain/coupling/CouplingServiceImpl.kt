package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.stereotype.Service

@Service
class CouplingServiceImpl(val jClassRepository: JClassRepository, val logicModuleRepository: LogicModuleRepository,
                          val dependencyService: DependencyService, val metricsRepository: MetricsRepository) : CouplingService {
    override fun persistAllClassCouplingResults(projectId: Long) {
        val classCouplingResults = getAllClassCouplingResults(projectId)
        metricsRepository.insertAllClassCouplings(projectId, classCouplingResults)
    }

    override fun getAllClassCouplingResults(projectId: Long): List<ClassCoupling> {
        val classes = jClassRepository.getJClassesHasModules(projectId)
        val logicModules = logicModuleRepository.getAllByProjectId(projectId)
        val classDependency = dependencyService.getAllClassDependencies(projectId)
        return classes.map { getClassCouplingWithData(it.toVO(), classDependency, logicModules) }
    }

    override fun calculateClassCoupling(projectId: Long, jClassVO: JClassVO): ClassCoupling {
        val jClassVOHasId = if (jClassVO.id != null) {
            jClassVO
        } else {
            val jClass = jClassRepository.getJClassBy(projectId, jClassVO.name, jClassVO.module)
                    ?: throw ClassNotFountException("Cannot found class with module: ${jClassVO.module} and name: ${jClassVO.name}")
            jClass.toVO()
        }
        val classCoupling = metricsRepository.getClassCoupling(jClassVOHasId)
        if (classCoupling != null) {
            return classCoupling
        }

        val logicModules = logicModuleRepository.getAllByProjectId(projectId)
        val classDependency = dependencyService.getAllClassDependencies(projectId)
        return getClassCouplingWithData(jClassVO, classDependency, logicModules)
    }

    override fun calculateClassCouplings(projectId: Long, jClassVOs: List<JClassVO>): List<ClassCoupling> {
        return jClassVOs.map { calculateClassCoupling(projectId, it) }
    }

    override fun calculatePackageCouplings(projectId: Long, packageVOs: List<PackageVO>): List<PackageCoupling> {
        return packageVOs.map { calculatePackageCoupling(projectId, it) }
    }

    override fun calculatePackageCoupling(projectId: Long, packageVO: PackageVO): PackageCoupling {
        val classes = jClassRepository.getAllByProjectId(projectId)
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToPackage.map { it.toVO() })
        if (classCouplingsCached.isNotEmpty()) {
            return PackageCoupling.of(packageVO, classCouplingsCached)
        }

        val logicModules = logicModuleRepository.getAllByProjectId(projectId)
        val classDependency = dependencyService.getAllClassDependencies(projectId)
        val classCouplings = classesBelongToPackage.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return PackageCoupling.of(packageVO, classCouplings)
    }

    override fun calculatePackageDirectClassCouplings(projectId: Long, packageVO: PackageVO): List<ClassCoupling> {
        val classes = jClassRepository.getAllByProjectId(projectId)
        val classesDirectBelongToPackage = classes.filter { packageVO.directContainClass(it.toVO()) }
        return calculateClassCouplings(projectId, classesDirectBelongToPackage.map { it.toVO() })
    }

    override fun calculateModuleCoupling(projectId: Long, logicModule: LogicModule): ModuleCoupling {
        val classes = jClassRepository.getAllByProjectId(projectId)
        val logicModules = logicModuleRepository.getAllByProjectId(projectId)
        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToModule.map { it.toVO() })
        if (classCouplingsCached.isNotEmpty()) {
            return ModuleCoupling.of(logicModule, classCouplingsCached)
        }

        val classDependency = dependencyService.getAllClassDependencies(projectId)
        val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
        return ModuleCoupling.of(logicModule, classCouplings)
    }

    override fun calculateAllModuleCoupling(projectId: Long): List<ModuleCoupling> {
        val classes = jClassRepository.getAllByProjectId(projectId)
        val logicModules = logicModuleRepository.getAllByProjectId(projectId)
        val moduleCouplings = mutableListOf<ModuleCoupling>()
        var classDependency = mutableListOf<Dependency<JClassVO>>()
        logicModules.parallelStream().forEach { logicModule ->
            val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
            val classCouplingsCached = metricsRepository.getClassCoupling(classesBelongToModule.map { it.toVO() })
            if (classCouplingsCached.isNotEmpty()) {
                moduleCouplings.add(ModuleCoupling.of(logicModule, classCouplingsCached))
                return@forEach
            }
            if (classDependency.isEmpty()) {
                classDependency.addAll(dependencyService.getAllClassDependencies(projectId))
            }
            val classCouplings = classesBelongToModule.map { it.toVO() }.map { getClassCouplingWithData(it, classDependency, logicModules) }
            moduleCouplings.add(ModuleCoupling.of(logicModule, classCouplings))
        }
        return moduleCouplings
    }

    fun getClassCouplingWithData(clazz: JClassVO, dependency: List<Dependency<JClassVO>>,
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
