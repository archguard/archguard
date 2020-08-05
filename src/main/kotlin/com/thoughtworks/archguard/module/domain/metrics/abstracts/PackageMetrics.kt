package com.thoughtworks.archguard.module.domain.metrics.abstracts

data class PackageMetrics(val ratio: Double, var packageName: String, val module: String)