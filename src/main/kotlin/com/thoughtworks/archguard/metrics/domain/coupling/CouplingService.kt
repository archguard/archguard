package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface CouplingService {
    fun persistAllClassCouplingResults()
    fun getAllClassCouplingResults(): List<ClassCoupling>
    fun calculateClassCoupling(jClassVO: JClassVO): ClassCoupling
    fun calculatePackageCoupling(packageVO: PackageVO): PackageCoupling
    fun calculateModuleCoupling(logicModule: LogicModule): ModuleCoupling
    fun calculateAllModuleCoupling(): List<ModuleCoupling>
}