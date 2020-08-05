package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleMetrics
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageMetrics
import com.thoughtworks.archguard.module.domain.model.LogicModule

interface AbstractAnalysisService {
    fun calculatePackageAbstractRatio(packageName: String, module: String): PackageMetrics
    fun calculateModuleAbstractRatio(logicModule: LogicModule): ModuleMetrics
}