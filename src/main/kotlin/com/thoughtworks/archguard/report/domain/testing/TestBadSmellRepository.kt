package com.thoughtworks.archguard.report.domain.testing

interface TestBadSmellRepository {

    fun getStaticMethodCount(systemId: Long): Long
    fun getStaticMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo>

    fun getSleepTestMethodCount(systemId: Long): Long
    fun getSleepTestMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo>

    fun getEmptyTestMethodCount(systemId: Long): Long
    fun getEmptyTestMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo>

    fun getIgnoreTestMethodCount(systemId: Long): Long
    fun getIgnoreTestMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo>

    fun getUnassertTestMethodIds(systemId: Long): List<String>
    fun getAssertMethodAboveThresholdIds(systemId: Long, multiAssertThreshold: Int): List<String>
    fun getRedundantPrintAboveThresholdIds(systemId: Long, redundantPrintThreshold: Int): List<String>

    fun getMethodsByIds(systemId: List<String>, limit: Long, offset: Long): List<MethodInfo>
}
