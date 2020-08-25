package com.thoughtworks.archguard.metrics.domain

interface ClassMetricRepository {
    fun insertClassMetricPOs(classMetricPOs: List<ClassMetricPO>)
}