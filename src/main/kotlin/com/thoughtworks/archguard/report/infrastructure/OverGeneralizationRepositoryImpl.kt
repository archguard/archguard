package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.overgeneralization.OverGeneralizationRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class OverGeneralizationRepositoryImpl(val jdbi: Jdbi) : OverGeneralizationRepository {
    override fun getOverGeneralizationCount(systemId: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getOverGeneralizationList(systemId: Long, limit: Long, offset: Long): List<ClassVO> {
        TODO("Not yet implemented")
    }
}