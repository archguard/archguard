package com.thoughtworks.archguard.report.domain.testing

interface TestingRepository {
    fun getStaticMethodCount(systemId: Long): Long
    fun getStaticMethods(systemId: Long, limit: Long, offset: Long): List<StaticMethod>

}
