package com.thoughtworks.archguard.metrics.domain.dfms

import org.archguard.model.vos.PackageVO
import com.thoughtworks.archguard.metrics.domain.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.metrics.domain.coupling.PackageCoupling

class PackageDfms private constructor(val packageVO: PackageVO, val innerInstabilityAvg: Double, val outerInstabilityAvg: Double, val absRatio: Double) {
    companion object {
        fun of(packageVO: PackageVO, packageCoupling: PackageCoupling, packageAbstractRatio: PackageAbstractRatio): PackageDfms {
            return PackageDfms(packageVO, packageCoupling.innerInstabilityAvg, packageCoupling.outerInstabilityAvg, packageAbstractRatio.ratio)
        }
    }
}
