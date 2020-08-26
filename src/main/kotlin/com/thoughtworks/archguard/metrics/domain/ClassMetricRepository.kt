package com.thoughtworks.archguard.metrics.domain

interface ClassMetricRepository {
    fun insertOrUpdateClassMetricPOs(projectId: Long, classMetricPOs: List<ClassMetricPO>)
}