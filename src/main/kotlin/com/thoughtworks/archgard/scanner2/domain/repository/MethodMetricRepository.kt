package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric

interface MethodMetricRepository {
    fun insertOrUpdateMethodMetric(systemId: Long, methodMetric: List<MethodMetric>)
}
