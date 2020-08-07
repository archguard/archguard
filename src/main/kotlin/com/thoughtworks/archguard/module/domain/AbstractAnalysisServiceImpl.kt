package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.stereotype.Service

@Service
class AbstractAnalysisServiceImpl(val jClassRepository: JClassRepository, val logicModuleRepository: LogicModuleRepository) : AbstractAnalysisService {
    override fun calculatePackageAbstractRatio(packageVO: PackageVO): PackageAbstractRatio {
        val classes = jClassRepository.getAll()
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val abstractClassesBelongToPackage = classesBelongToPackage.filter { it.isAbstractClass() || it.isInterface() }
        return PackageAbstractRatio(abstractClassesBelongToPackage.size.toDouble() / classesBelongToPackage.size, packageVO)
    }

    override fun calculateModuleAbstractRatio(logicModule: LogicModule): ModuleAbstractRatio {
        val classes = jClassRepository.getAll()
        val logicModules = logicModuleRepository.getAll()
        val classesBelongToModule = classes.filter { getModule(logicModules, it.toVO()).contains(logicModule) }
        val abstractClassesBelongToModule = classesBelongToModule.filter { it.isAbstractClass() || it.isInterface() }
        return ModuleAbstractRatio(abstractClassesBelongToModule.size.toDouble() / classesBelongToModule.size, logicModule)
    }
}