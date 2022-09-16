package com.thoughtworks.archguard.change.domain.repository

import com.thoughtworks.archguard.change.domain.model.DiffChange

interface DiffChangeRepository {
    fun findBySystemId(systemId: Long): List<DiffChange>
}
