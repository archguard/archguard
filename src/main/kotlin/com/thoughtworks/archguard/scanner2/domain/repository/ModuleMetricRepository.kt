package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.ModuleMetric

interface ModuleMetricRepository {
    fun insertOrUpdateModuleMetric(systemId: Long, moduleMetrics: List<ModuleMetric>)

}
