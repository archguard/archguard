package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.models.ClassVO

interface RedundancyRepository {
    fun getOneMethodClassCount(systemId: Long): Long
    fun getOneMethodClass(systemId: Long, limit: Long, offset: Long): List<ClassVO>
}
