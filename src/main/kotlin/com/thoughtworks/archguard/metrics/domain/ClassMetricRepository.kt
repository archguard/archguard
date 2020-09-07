package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.metrics.appl.ClassMetricPO

interface ClassMetricRepository {
    fun insertOrUpdateClassMetricPOs(systemId: Long, classMetricPOs: List<ClassMetricPO>)

    fun getClassDitExceedThreshold(systemId: Long, threshold: Int,
                                   limitPerPage: Int, numOfPage: Int): List<ClassDit>
}
