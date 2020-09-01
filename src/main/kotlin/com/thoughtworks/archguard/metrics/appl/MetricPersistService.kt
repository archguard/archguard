package com.thoughtworks.archguard.metrics.appl

interface MetricPersistService {
    fun persistClassMetrics(systemId: Long)
}
