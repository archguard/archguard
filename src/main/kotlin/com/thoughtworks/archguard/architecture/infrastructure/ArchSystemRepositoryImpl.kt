package com.thoughtworks.archguard.architecture.infrastructure

import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemPO
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ArchSystemRepositoryImpl : ArchSystemRepository {
    private var archSystems: Map<String, ArchSystemPO> = HashMap()

    override fun getArchSystem(id: String): Optional<ArchSystemPO> {
        return Optional.ofNullable(archSystems[id])
    }

    override fun createArchSystem(archSystemPO: ArchSystemPO): Boolean {
        if (archSystems.containsKey(archSystemPO.id)) {
            return false
        }

        archSystems.plus(Pair(archSystemPO.id, archSystemPO))

        return true
    }
}
