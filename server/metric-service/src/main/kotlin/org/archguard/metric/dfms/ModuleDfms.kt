package org.archguard.metric.dfms

import org.archguard.arch.LogicModule

class ModuleDfms private constructor(val logicModule: LogicModule, val innerInstabilityAvg: Double, val outerInstabilityAvg: Double, val absRatio: Double) {
    companion object {
        fun of(logicModule: LogicModule, moduleCoupling: com.thoughtworks.archguard.metrics.domain.coupling.ModuleCoupling, moduleAbstractRatio: com.thoughtworks.archguard.metrics.domain.abstracts.ModuleAbstractRatio): ModuleDfms {
            return ModuleDfms(
                logicModule,
                moduleCoupling.innerInstabilityAvg,
                moduleCoupling.outerInstabilityAvg,
                moduleAbstractRatio.ratio
            )
        }
    }
}