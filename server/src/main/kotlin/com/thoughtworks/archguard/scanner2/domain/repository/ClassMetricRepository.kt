package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.metric.ClassMetric

interface ClassMetricRepository {
    fun insertOrUpdateClassMetric(systemId: Long, classMetrics: List<ClassMetric>)
}
