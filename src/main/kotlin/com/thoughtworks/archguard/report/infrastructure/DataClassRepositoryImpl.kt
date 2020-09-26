package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class DataClassRepositoryImpl(val jdbi: Jdbi) : DataClassRepository {
    override fun getAllDataClassCount(systemId: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getAllDataClassWithOnlyOneFieldCount(systemId: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getAllDataClass(systemId: Long, offset: Long, limit: Long): List<DataClass> {
        TODO("Not yet implemented")
    }

    override fun getAllDataClassWithOnlyOneField(systemId: Long, offset: Long, limit: Long): List<DataClass> {
        TODO("Not yet implemented")
    }

}