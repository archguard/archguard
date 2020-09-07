package com.thoughtworks.archguard.report.domain.hub

interface HubRepository {
    fun getClassAboveHubThresholdCount(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int): Long
    fun getClassListAboveHubThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): List<ClassHub>
}