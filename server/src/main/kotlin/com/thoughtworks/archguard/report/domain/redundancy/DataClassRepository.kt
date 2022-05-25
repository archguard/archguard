package com.thoughtworks.archguard.report.domain.redundancy

interface DataClassRepository {
    fun getAllDataClass(systemId: Long, offset: Long, limit: Long): List<DataClass>

    fun getAllDataClassCount(systemId: Long): Long

    fun getAllDataClassWithOnlyOneField(systemId: Long, offset: Long, limit: Long): List<DataClass>

    fun getAllDataClassWithOnlyOneFieldCount(systemId: Long): Long
}
