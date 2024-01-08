package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.metric.ModuleMetric

interface ModuleMetricRepository {
    fun insertOrUpdateModuleMetric(systemId: Long, moduleMetrics: List<ModuleMetric>)
}
