package com.thoughtworks.archguard.module.domain.metrics.abstracts

import com.thoughtworks.archguard.module.domain.model.PackageVO

data class PackageMetrics(val ratio: Double, val packageVO: PackageVO)