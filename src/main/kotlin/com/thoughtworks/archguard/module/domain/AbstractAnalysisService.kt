package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleMetrics
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageMetrics
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface AbstractAnalysisService {
    fun calculatePackageAbstractRatio(packageVO: PackageVO): PackageMetrics
    fun calculateModuleAbstractRatio(logicModule: LogicModule): ModuleMetrics
}