package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface AbstractAnalysisService {
    fun calculatePackageAbstractRatio(projectId:Long, packageVO: PackageVO): PackageAbstractRatio
    fun calculateModuleAbstractRatio(projectId:Long, logicModule: LogicModule): ModuleAbstractRatio
}
