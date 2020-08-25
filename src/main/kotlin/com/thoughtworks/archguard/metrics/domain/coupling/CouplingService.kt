package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface CouplingService {
    fun persistAllClassCouplingResults(projectId:Long)
    fun getAllClassCouplingResults(projectId:Long): List<ClassCoupling>
    fun calculateClassCoupling(projectId:Long, jClassVO: JClassVO): ClassCoupling
    fun calculateClassCouplings(projectId:Long,jClassVOs: List<JClassVO>): List<ClassCoupling>
    fun calculatePackageDirectClassCouplings(projectId:Long, packageVO: PackageVO): List<ClassCoupling>
    fun calculatePackageCoupling(projectId:Long, packageVO: PackageVO): PackageCoupling
    fun calculatePackageCouplings(projectId:Long, packageVOs: List<PackageVO>): List<PackageCoupling>
    fun calculateModuleCoupling(projectId:Long, logicModule: LogicModule): ModuleCoupling
    fun calculateAllModuleCoupling(projectId:Long): List<ModuleCoupling>
}
