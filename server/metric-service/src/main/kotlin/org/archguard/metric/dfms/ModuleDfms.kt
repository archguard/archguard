package org.archguard.metric.dfms

import org.archguard.arch.LogicModule
import org.archguard.metric.abstracts.ModuleAbstractRatio
import org.archguard.metric.coupling.ModuleCoupling

class ModuleDfms private constructor(val logicModule: LogicModule, val innerInstabilityAvg: Double, val outerInstabilityAvg: Double, val absRatio: Double) {
    companion object {
        fun of(logicModule: LogicModule, moduleCoupling: ModuleCoupling, moduleAbstractRatio: ModuleAbstractRatio): ModuleDfms {
            return ModuleDfms(
                logicModule,
                moduleCoupling.innerInstabilityAvg,
                moduleCoupling.outerInstabilityAvg,
                moduleAbstractRatio.ratio
            )
        }
    }
}