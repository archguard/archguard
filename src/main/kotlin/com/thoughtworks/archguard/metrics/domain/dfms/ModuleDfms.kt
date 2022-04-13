package com.thoughtworks.archguard.metrics.domain.dfms

import com.thoughtworks.archguard.metrics.domain.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleCoupling
import com.thoughtworks.archguard.code.module.domain.model.LogicModule

class ModuleDfms private constructor(val logicModule: LogicModule, val innerInstabilityAvg: Double, val outerInstabilityAvg: Double, val absRatio: Double) {
    companion object {
        fun of(logicModule: LogicModule, moduleCoupling: ModuleCoupling, moduleAbstractRatio: ModuleAbstractRatio): ModuleDfms {
            return ModuleDfms(logicModule, moduleCoupling.innerInstabilityAvg, moduleCoupling.outerInstabilityAvg, moduleAbstractRatio.ratio)
        }
    }

}
