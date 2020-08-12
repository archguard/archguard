package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.metrics.domain.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetrics
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface MetricsService {
    fun calculateCoupling()
    fun getAllMetrics(): List<ModuleMetrics>
    fun getModuleMetrics(): List<ModuleMetrics>

    fun getClassAbstractMetric(jClassVO: JClassVO): ClassAbstractRatio
    fun getPackageAbstractMetric(packageVO: PackageVO): PackageAbstractRatio
    fun getModuleAbstractMetric(moduleName: String): ModuleAbstractRatio

    fun getClassNoc(jClassVO: JClassVO): Int

    fun getClassAbc(jClassVO: JClassVO): Int
}