package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric


interface MetricRepository {
    fun insertOrUpdateClassMetric(systemId: Long, classMetricPOs: List<ClassMetric>)
}
