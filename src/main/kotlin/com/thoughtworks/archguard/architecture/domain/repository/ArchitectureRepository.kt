package com.thoughtworks.archguard.architecture.domain.repository

import java.util.Optional

interface ArchitectureRepository {
    fun getArchitecture(archSystemId: String): Optional<ArchitecturePO>

    fun saveArchitecture(architecturePO: ArchitecturePO): ArchitecturePO
}
