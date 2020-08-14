package com.thoughtworks.archguard.metrics.domain.coupling

interface MetricsRepository {
    fun insert(moduleMetrics: List<ModuleMetricsLegacy>)
    fun findAllMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy>
    fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy>
}