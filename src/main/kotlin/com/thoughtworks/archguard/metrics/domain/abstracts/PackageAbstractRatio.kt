package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.code.module.domain.model.PackageVO

data class PackageAbstractRatio(val ratio: Double, val packageVO: PackageVO)