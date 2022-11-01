package com.thoughtworks.archguard.architecture.infrastructure

import com.thoughtworks.archguard.architecture.domain.model.ArchSystem
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ArchSystemRepositoryImpl : ArchSystemRepository {
    private var archSystems: MutableMap<String, ArchSystem> = HashMap()

    override fun getById(id: String): Optional<ArchSystem> {
        return Optional.ofNullable(archSystems[id])
    }

    override fun findAll(): List<ArchSystem> {
        return archSystems.values.toList()
    }

    override fun create(archSystem: ArchSystem) {
        if (archSystems.containsKey(archSystem.id)) {
            throw Exception()
        }

        archSystems[archSystem.id] = archSystem
    }
}
