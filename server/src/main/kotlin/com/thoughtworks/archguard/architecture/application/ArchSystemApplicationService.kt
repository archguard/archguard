package com.thoughtworks.archguard.architecture.application

import com.thoughtworks.archguard.architecture.application.request.ArchSystemCreateRequest
import com.thoughtworks.archguard.architecture.application.response.ArchSystemCreateResponse
import com.thoughtworks.archguard.architecture.domain.model.ArchSystem
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import org.springframework.stereotype.Service

@Service
class ArchSystemApplicationService(val archSystemRepository: ArchSystemRepository) {

    fun createArchSystem(request: ArchSystemCreateRequest): ArchSystemCreateResponse {
        val name = request.name

        val archSystem = ArchSystem.build()
        archSystem.name = name

        archSystemRepository.create(archSystem)

        return ArchSystemCreateResponse.from(archSystem)
    }

}