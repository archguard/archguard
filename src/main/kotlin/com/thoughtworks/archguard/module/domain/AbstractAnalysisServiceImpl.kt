package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleMetrics
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageMetrics
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.stereotype.Service

@Service
class AbstractAnalysisServiceImpl(val jClassRepository: JClassRepository, val logicModuleRepository: LogicModuleRepository) : AbstractAnalysisService {
    override fun calculatePackageAbstractRatio(packageVO: PackageVO): PackageMetrics {
        val classes = jClassRepository.getAll()
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val abstractClassesBelongToPackage = classesBelongToPackage.filter { it.isAbstractClass() || it.isInterface() }
        return PackageMetrics(abstractClassesBelongToPackage.size.toDouble() / classesBelongToPackage.size, packageVO)
    }

    override fun calculateModuleAbstractRatio(logicModule: LogicModule): ModuleMetrics {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val abstractClassesBelongToModule = classesBelongToModule.filter { it.isAbstractClass() || it.isInterface() }
        return ModuleMetrics(abstractClassesBelongToModule.size.toDouble() / classesBelongToModule.size, logicModule)
    }
}