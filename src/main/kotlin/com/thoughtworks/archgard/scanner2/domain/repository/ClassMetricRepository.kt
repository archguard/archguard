package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric


interface ClassMetricRepository {
    fun insertOrUpdateClassMetric(systemId: Long, classMetrics: List<ClassMetric>)
}
