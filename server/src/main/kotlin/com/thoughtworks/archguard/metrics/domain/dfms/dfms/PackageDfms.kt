package com.thoughtworks.archguard.metrics.domain.dfms.dfms

import org.archguard.model.vos.PackageVO

class PackageDfms private constructor(val packageVO: PackageVO, val innerInstabilityAvg: Double, val outerInstabilityAvg: Double, val absRatio: Double) {
    companion object {
        fun of(packageVO: PackageVO, packageCoupling: com.thoughtworks.archguard.metrics.domain.coupling.PackageCoupling, packageAbstractRatio: com.thoughtworks.archguard.metrics.domain.abstracts.PackageAbstractRatio): PackageDfms {
            return PackageDfms(
                packageVO,
                packageCoupling.innerInstabilityAvg,
                packageCoupling.outerInstabilityAvg,
                packageAbstractRatio.ratio
            )
        }
    }
}