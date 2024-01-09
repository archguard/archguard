package org.archguard.metric.dfms

import org.archguard.metric.abstracts.PackageAbstractRatio
import org.archguard.metric.coupling.PackageCoupling
import org.archguard.model.vos.PackageVO

class PackageDfms private constructor(val packageVO: PackageVO, val innerInstabilityAvg: Double, val outerInstabilityAvg: Double, val absRatio: Double) {
    companion object {
        fun of(packageVO: PackageVO, packageCoupling: PackageCoupling, packageAbstractRatio: PackageAbstractRatio): PackageDfms {
            return PackageDfms(
                packageVO,
                packageCoupling.innerInstabilityAvg,
                packageCoupling.outerInstabilityAvg,
                packageAbstractRatio.ratio
            )
        }
    }
}