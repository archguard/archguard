package org.archguard.metric.dfms

import org.archguard.model.vos.JClassVO

class ClassDfms private constructor(
    val jClassVO: JClassVO,
    val innerInstability: Double,
    val outerInstability: Double,
    val absRatio: Double
) {
    companion object {
        fun of(
            jClassVO: JClassVO,
            classCoupling: com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling,
            classAbstractRatio: com.thoughtworks.archguard.metrics.domain.abstracts.ClassAbstractRatio
        ): ClassDfms {
            return ClassDfms(
                jClassVO,
                classCoupling.innerInstability,
                classCoupling.outerInstability,
                classAbstractRatio.ratio
            )
        }
    }
}