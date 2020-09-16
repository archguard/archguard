package com.thoughtworks.archguard.report.domain.circulardependency

interface CircularDependencyRepository {
    fun getCircularDependency(systemId: Long, type: CircularDependencyType, limit: Long, offset: Long): List<String>
    fun getCircularDependencyCount(systemId: Long, type: CircularDependencyType): Long
}