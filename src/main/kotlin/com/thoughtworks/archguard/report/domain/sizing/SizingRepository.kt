package com.thoughtworks.archguard.report.domain.sizing


interface SizingRepository {
    fun getMethodSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getMethodSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodSizing>

    fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getClassSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithLine>

    fun getClassSizingListAboveMethodCountThresholdCount(systemId: Long, threshold: Int): Long
    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithMethodCount>
}