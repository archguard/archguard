package com.thoughtworks.archgard.scanner2.domain

import com.thoughtworks.archgard.scanner2.infrastructure.persist.ClassMetricPO


interface MetricRepository {
    fun insertOrUpdateClassMetricPOs(systemId: Long, classMetricPOs: List<ClassMetricPO>)
}
