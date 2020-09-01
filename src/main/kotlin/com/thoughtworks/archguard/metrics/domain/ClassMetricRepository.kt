package com.thoughtworks.archguard.metrics.domain

interface ClassMetricRepository {
    fun insertOrUpdateClassMetricPOs(systemId: Long, classMetricPOs: List<ClassMetricPO>)
}
