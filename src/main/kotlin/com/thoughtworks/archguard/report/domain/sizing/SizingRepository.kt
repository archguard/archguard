package com.thoughtworks.archguard.report.domain.sizing

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult


interface SizingRepository {
    fun getModuleSizingListAbovePackageCountThresholdCount(systemId: Long, threshold: Int): Long
    fun getModuleSizingListAbovePackageCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ModuleSizing>
    fun getModuleSizingListAbovePackageCountBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult

    fun getModuleSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getModuleSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ModuleSizing>
    fun getModuleSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult

    fun getPackageSizingListAboveClassCountThresholdCount(systemId: Long, threshold: Int): Long
    fun getPackageSizingListAboveClassCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing>
    fun getPackageSizingListAboveClassCountBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult

    fun getPackageSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getPackageSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<PackageSizing>
    fun getPackageSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult

    fun getMethodSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getMethodSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<MethodSizing>
    fun getMethodSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult

    fun getClassSizingAboveLineThresholdCount(systemId: Long, threshold: Int): Long
    fun getClassSizingAboveLineThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithLine>
    fun getClassSizingAboveLineBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult

    fun getClassSizingListAboveMethodCountThresholdCount(systemId: Long, threshold: Int): Long
    fun getClassSizingListAboveMethodCountThreshold(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassSizingWithMethodCount>
    fun getClassSizingListAboveMethodCountBadSmellResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult
}
