package com.thoughtworks.archguard.change.domain

interface DiffChangeRepo {
    fun findBySystemId(systemId: Long): List<DiffChange>
}
