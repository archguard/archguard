package com.thoughtworks.archguard.architecture.domain.service

import com.thoughtworks.archguard.architecture.domain.model.ArchSystem
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemPO
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ArchSystemService(var archSystemRepository: ArchSystemRepository) {

    fun create(name: String): ArchSystem {
        val archSystem = ArchSystem(UUID.randomUUID().toString(), name, ArrayList())

        archSystemRepository.createArchSystem(ArchSystemPO.from(archSystem))

        return archSystem
    }
}
