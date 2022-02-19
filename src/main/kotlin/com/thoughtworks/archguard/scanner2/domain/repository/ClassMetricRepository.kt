package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.ClassMetric


interface ClassMetricRepository {
    fun insertOrUpdateClassMetric(systemId: Long, classMetrics: List<ClassMetric>)
}
