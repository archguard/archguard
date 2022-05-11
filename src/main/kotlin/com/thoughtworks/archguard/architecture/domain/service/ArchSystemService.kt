package com.thoughtworks.archguard.architecture.domain.service

import com.thoughtworks.archguard.architecture.domain.model.ArchSystem
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ArchSystemService {

    fun create(name: String): ArchSystem {
        return ArchSystem(UUID.randomUUID().toString(), name, listOf())
    }
}
