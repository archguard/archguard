package com.thoughtworks.archguard.architecture.domain.repository

import com.thoughtworks.archguard.architecture.domain.model.analyze.ArchSystem
import java.util.Optional

interface ArchSystemRepository {
    fun getById(id: String): Optional<ArchSystem>

    fun findAll(): List<ArchSystem>

    fun create(archSystem: ArchSystem)
}
