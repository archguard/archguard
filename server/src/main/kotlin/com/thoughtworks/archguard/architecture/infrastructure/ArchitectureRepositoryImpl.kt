package com.thoughtworks.archguard.architecture.infrastructure

import com.thoughtworks.archguard.architecture.domain.repository.ArchitecturePO
import com.thoughtworks.archguard.architecture.domain.repository.ArchitectureRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ArchitectureRepositoryImpl : ArchitectureRepository {
    private var architectures: Map<String, ArchitecturePO> = HashMap()

    override fun getByArchSystemId(archSystemId: String): Optional<ArchitecturePO> {
        return Optional.ofNullable(architectures[archSystemId])
    }

    override fun save(architecturePO: ArchitecturePO): ArchitecturePO {
        architectures.plus(Pair(architecturePO.archSystemId, architecturePO))

        return architecturePO
    }
}
