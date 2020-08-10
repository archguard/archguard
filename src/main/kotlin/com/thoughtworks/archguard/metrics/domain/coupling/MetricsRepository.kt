package com.thoughtworks.archguard.metrics.domain.coupling

interface MetricsRepository {
    fun insert(moduleMetrics: List<ModuleMetrics>)
    fun findAllMetrics(moduleNames: List<String>): List<ModuleMetrics>
    fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetrics>
}