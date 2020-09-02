package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.metrics.appl.ClassMetricPO

interface ClassMetricRepository {
    fun insertOrUpdateClassMetricPOs(systemId: Long, classMetricPOs: List<ClassMetricPO>)

    fun getClassLCOM4ExceedThreshold(systemId: Long, threshold: Int): List<ClassLCOM4>
    fun getClassDitExceedThreshold(systemId: Long, threshold: Int): List<ClassDit>
}
