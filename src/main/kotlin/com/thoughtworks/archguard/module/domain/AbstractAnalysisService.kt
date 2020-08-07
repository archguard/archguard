package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface AbstractAnalysisService {
    fun calculatePackageAbstractRatio(packageVO: PackageVO): PackageAbstractRatio
    fun calculateModuleAbstractRatio(logicModule: LogicModule): ModuleAbstractRatio
}