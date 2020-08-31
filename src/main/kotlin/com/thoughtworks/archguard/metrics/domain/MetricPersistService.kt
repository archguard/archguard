package com.thoughtworks.archguard.metrics.domain

interface MetricPersistService {
    fun persistClassMetrics(systemId: Long)
}
