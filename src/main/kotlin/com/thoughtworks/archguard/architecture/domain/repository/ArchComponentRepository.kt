package com.thoughtworks.archguard.architecture.domain.repository

import java.util.Optional

interface ArchComponentRepository {
    fun getById(id: String): Optional<ArchComponentPO>

    fun getByArchSystemId(id: String): Optional<ArchComponentPO>

    fun create(archComponentPO: ArchComponentPO): Boolean
}
