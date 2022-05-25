package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.DiffChange
import com.thoughtworks.archguard.change.domain.DiffChangeRepo
import org.springframework.stereotype.Repository

@Repository
class DiffChangeRepoImpl(val changeDao: DiffChangeDao) : DiffChangeRepo {
    override fun findBySystemId(systemId: Long): List<DiffChange> {
        return changeDao.findBySystemId(systemId)
    }
}
