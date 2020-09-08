package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric


interface MetricRepository {
    fun insertOrUpdateClassMetric(systemId: Long, classMetricPOs: List<ClassMetric>)
}
