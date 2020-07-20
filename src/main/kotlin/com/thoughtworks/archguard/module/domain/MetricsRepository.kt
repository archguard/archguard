package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.ModuleMetrics

interface MetricsRepository {
    fun insert(moduleMetrics: List<ModuleMetrics>)
}