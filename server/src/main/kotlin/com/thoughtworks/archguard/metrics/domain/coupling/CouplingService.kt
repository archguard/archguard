package com.thoughtworks.archguard.metrics.domain.coupling

import org.archguard.arch.LogicModule
import org.archguard.metric.coupling.ClassCoupling
import org.archguard.metric.coupling.ModuleCoupling
import org.archguard.metric.coupling.PackageCoupling
import org.archguard.model.vos.PackageVO
import org.archguard.model.vos.JClassVO

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
