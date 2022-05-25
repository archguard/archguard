package com.thoughtworks.archguard.architecture.domain.repository

import java.util.Optional

interface ArchitectureRepository {
    fun getByArchSystemId(archSystemId: String): Optional<ArchitecturePO>

    fun save(architecturePO: ArchitecturePO): ArchitecturePO
}
