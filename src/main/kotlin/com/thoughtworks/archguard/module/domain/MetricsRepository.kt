package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.metrics.coupling.ModuleMetrics

interface MetricsRepository {
    fun insert(moduleMetrics: List<ModuleMetrics>)
    fun findAllMetrics(moduleNames: List<String>): List<ModuleMetrics>
    fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetrics>
}