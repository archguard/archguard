package com.thoughtworks.archguard.architecture.domain.repository

import java.util.Optional

interface ArchSystemRepository {
    fun getById(id: String): Optional<ArchSystemPO>

    fun create(archSystemPO: ArchSystemPO): Boolean
}
