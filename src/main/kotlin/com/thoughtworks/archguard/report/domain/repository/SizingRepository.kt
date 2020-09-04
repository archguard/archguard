package com.thoughtworks.archguard.report.domain.repository

import com.thoughtworks.archguard.report.domain.model.ClassSizingWithLine
import com.thoughtworks.archguard.report.domain.model.MethodSizing


interface SizingRepository {
    fun getMethodSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long

    fun getMethodSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodSizing>
    fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getClassSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithLine>
}