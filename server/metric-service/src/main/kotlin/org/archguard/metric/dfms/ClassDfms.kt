package org.archguard.metric.dfms

import org.archguard.metric.abstracts.ClassAbstractRatio
import org.archguard.metric.coupling.ClassCoupling
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
            classCoupling: ClassCoupling,
            classAbstractRatio: ClassAbstractRatio
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