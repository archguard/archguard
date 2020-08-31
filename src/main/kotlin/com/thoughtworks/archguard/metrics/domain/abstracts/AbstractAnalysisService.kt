package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface AbstractAnalysisService {
    fun calculatePackageAbstractRatio(systemId:Long, packageVO: PackageVO): PackageAbstractRatio
    fun calculateModuleAbstractRatio(systemId:Long, logicModule: LogicModule): ModuleAbstractRatio
}
