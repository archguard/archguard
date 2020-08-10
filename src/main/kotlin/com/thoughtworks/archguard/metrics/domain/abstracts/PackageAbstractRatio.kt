package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.module.domain.model.PackageVO

data class PackageAbstractRatio(val ratio: Double, val packageVO: PackageVO)