package com.thoughtworks.archguard.architecture.domain.service

import com.thoughtworks.archguard.architecture.domain.model.ArchStyle
import com.thoughtworks.archguard.architecture.domain.model.ArchSystem
import com.thoughtworks.archguard.architecture.domain.model.Architecture
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemPO
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import com.thoughtworks.archguard.architecture.domain.repository.ArchitecturePO
import com.thoughtworks.archguard.architecture.domain.repository.ArchitectureRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ArchSystemService(
    var archSystemRepository: ArchSystemRepository,
    var architectureRepository: ArchitectureRepository
) {

    fun create(name: String, style: ArchStyle): ArchSystem {
        val archSystem = ArchSystem(UUID.randomUUID().toString(), name, null, ArrayList())

        val architecture = Architecture(archSystem.id, style, ArrayList(), ArrayList())

        archSystem.architecture = architecture

        archSystemRepository.createArchSystem(ArchSystemPO.from(archSystem))
        architectureRepository.saveArchitecture(ArchitecturePO.from(architecture))

        return archSystem
    }
}
