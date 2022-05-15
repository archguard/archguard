package com.thoughtworks.archguard.architecture.domain.repository

import java.util.Optional

interface ArchSystemRepository {
    fun getArchSystem(id: String): Optional<ArchSystemPO>

    fun createArchSystem(archSystemPO: ArchSystemPO): Boolean
}
