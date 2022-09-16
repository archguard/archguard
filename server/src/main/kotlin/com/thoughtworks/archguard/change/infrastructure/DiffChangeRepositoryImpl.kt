package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.model.DiffChange
import com.thoughtworks.archguard.change.domain.repository.DiffChangeRepository
import org.springframework.stereotype.Repository

@Repository
class DiffChangeRepositoryImpl(val changeDao: DiffChangeDao) : DiffChangeRepository {
    override fun findBySystemId(systemId: Long): List<DiffChange> {
        return changeDao.findBySystemId(systemId)
    }
}
