package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import org.archguard.arch.LogicModule
import org.archguard.arch.LogicModuleUtil
import org.archguard.metric.abstracts.ModuleAbstractRatio
import org.archguard.metric.abstracts.PackageAbstractRatio
import org.archguard.model.vos.PackageVO
import org.springframework.stereotype.Service

@Service
class AbstractAnalysisService(
    val jClassRepository: JClassRepository,
    val logicModuleRepository: LogicModuleRepository
) {

    fun calculatePackageAbstractRatio(systemId: Long, packageVO: PackageVO): PackageAbstractRatio {
        val classes = jClassRepository.getAllBySystemId(systemId)
        val classesBelongToPackage = classes.filter { packageVO.containClass(it.toVO()) }
        val abstractClassesBelongToPackage = classesBelongToPackage.filter { it.isAbstractClass() || it.isInterface() }
        return PackageAbstractRatio(abstractClassesBelongToPackage.size.toDouble() / classesBelongToPackage.size, packageVO)
    }

    fun calculateModuleAbstractRatio(systemId: Long, logicModule: LogicModule): ModuleAbstractRatio {
        val classes = jClassRepository.getAllBySystemId(systemId)
        val logicModules = logicModuleRepository.getAllBySystemId(systemId)
        val classesBelongToModule = classes.filter {
            LogicModuleUtil.getModule(logicModules, it.toVO()).contains(logicModule)
        }
        val abstractClassesBelongToModule = classesBelongToModule.filter { it.isAbstractClass() || it.isInterface() }
        return ModuleAbstractRatio(abstractClassesBelongToModule.size.toDouble() / classesBelongToModule.size, logicModule)
    }
}
