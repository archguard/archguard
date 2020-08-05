package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleMetrics
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageMetrics
import com.thoughtworks.archguard.module.domain.model.LogicModule
import org.springframework.stereotype.Service

@Service
class AbstractAnalysisServiceImpl : AbstractAnalysisService {
    override fun calculatePackageAbstractRatio(packageName: String, module: String): PackageMetrics {
        TODO("Not yet implemented")
    }

    override fun calculateModuleAbstractRatio(logicModule: LogicModule): ModuleMetrics {
        TODO("Not yet implemented")
    }
}