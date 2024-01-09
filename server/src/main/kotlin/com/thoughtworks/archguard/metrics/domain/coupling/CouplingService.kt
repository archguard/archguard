package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import org.archguard.arch.LogicModule
import com.thoughtworks.archguard.code.module.domain.model.PackageVO

interface CouplingService {
    fun persistAllClassCouplingResults(systemId: Long)
    fun getAllClassCouplingResults(systemId: Long): List<ClassCoupling>
    fun calculateClassCoupling(systemId: Long, jClassVO: JClassVO): ClassCoupling
    fun calculateClassCouplings(systemId: Long, jClassVOs: List<JClassVO>): List<ClassCoupling>
    fun calculatePackageDirectClassCouplings(systemId: Long, packageVO: PackageVO): List<ClassCoupling>
    fun calculatePackageCoupling(systemId: Long, packageVO: PackageVO): PackageCoupling
    fun calculatePackageCouplings(systemId: Long, packageVOs: List<PackageVO>): List<PackageCoupling>
    fun calculateModuleCoupling(systemId: Long, logicModule: LogicModule): ModuleCoupling
    fun calculateAllModuleCoupling(systemId: Long): List<ModuleCoupling>
}
