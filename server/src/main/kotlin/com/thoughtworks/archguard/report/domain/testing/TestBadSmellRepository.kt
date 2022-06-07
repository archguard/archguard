package com.thoughtworks.archguard.report.domain.testing

import com.thoughtworks.archguard.report.infrastructure.TestSmellPO

interface TestBadSmellRepository {
    fun countTestSmellByType(systemId: Long, type: String): Long
    fun getTestSmellByType(systemId: Long, type: String, limit: Long, offset: Long): List<TestSmellPO>
    fun getStaticMethodCount(systemId: Long): Long
    fun getStaticMethods(systemId: Long, limit: Long, offset: Long): List<MethodInfo>
}
