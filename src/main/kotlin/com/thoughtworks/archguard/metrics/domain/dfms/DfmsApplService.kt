package com.thoughtworks.archguard.metrics.domain.dfms

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.metrics.domain.abstracts.AbstractAnalysisService
import com.thoughtworks.archguard.metrics.domain.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.metrics.domain.coupling.CouplingService
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.stereotype.Service

@Service
class DfmsApplService(
        val logicModuleRepository: LogicModuleRepository,
        val jClassRepository: JClassRepository,
        val abstractAnalysisService: AbstractAnalysisService,
        val jMethodRepository: JMethodRepository,
        val couplingService: CouplingService) {

    fun getPackageDfms(systemId: Long, packageVO: PackageVO): PackageDfms {
        return PackageDfms.of(packageVO,
                couplingService.calculatePackageCoupling(systemId, packageVO),
                abstractAnalysisService.calculatePackageAbstractRatio(systemId, packageVO))
    }

    fun getModuleDfms(systemId: Long, moduleName: String): ModuleDfms {
        val logicModule = logicModuleRepository.get(systemId, moduleName)
        return ModuleDfms.of(logicModule,
                couplingService.calculateModuleCoupling(systemId, logicModule),
                abstractAnalysisService.calculateModuleAbstractRatio(systemId, logicModule))
    }

    fun getClassDfms(systemId: Long, jClassVO: JClassVO): ClassDfms {
        return ClassDfms.of(jClassVO,
                couplingService.calculateClassCoupling(systemId, jClassVO),
                getClassAbstractMetric(systemId, jClassVO))
    }

    private fun getClassAbstractMetric(systemId: Long, jClassVO: JClassVO): ClassAbstractRatio {
        val jClass = jClassRepository.getJClassBy(systemId, jClassVO.name, jClassVO.module)
                ?: throw ClassNotFountException("Not Found JClass with Module ${jClassVO.module} and ClassName ${jClassVO.name}")
        val methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        jClass.methods = methods
        return ClassAbstractRatio.fromJClass(jClass)
    }

}
