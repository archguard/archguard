package com.thoughtworks.archguard.metrics.domain.abstracts

import org.archguard.model.vos.PackageVO

data class PackageAbstractRatio(val ratio: Double, val packageVO: PackageVO)
