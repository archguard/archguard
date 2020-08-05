package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.coupling.ModuleMetrics

interface MetricsService {
    fun calculateCoupling()
    fun getAllMetrics(): List<ModuleMetrics>
    fun getModuleMetrics(): List<ModuleMetrics>
}