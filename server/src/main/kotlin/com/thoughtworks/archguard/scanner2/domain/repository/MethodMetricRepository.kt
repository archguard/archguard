package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.metric.MethodMetric

interface MethodMetricRepository {
    fun insertOrUpdateMethodMetric(systemId: Long, methodMetric: List<MethodMetric>)
}
