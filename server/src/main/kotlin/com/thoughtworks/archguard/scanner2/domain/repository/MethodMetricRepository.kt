package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.MethodMetric

interface MethodMetricRepository {
    fun insertOrUpdateMethodMetric(systemId: Long, methodMetric: List<MethodMetric>)
}
