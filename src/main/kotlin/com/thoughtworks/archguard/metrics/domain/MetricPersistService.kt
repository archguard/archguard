package com.thoughtworks.archguard.metrics.domain

interface MetricPersistService {
    fun persistClassMetrics(projectId: Long)
}
