package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.coupling.ModuleMetrics
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface MetricsService {
    fun calculateCoupling()
    fun getAllMetrics(): List<ModuleMetrics>
    fun getModuleMetrics(): List<ModuleMetrics>
    fun getClassAbstractMetric(jClassVO: JClassVO): ClassAbstractRatio
    fun getPackageAbstractMetric(packageVO: PackageVO): PackageAbstractRatio
    fun getModuleAbstractMetric(moduleName: String): ModuleAbstractRatio
}