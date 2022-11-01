package com.thoughtworks.archguard.architecture.application.response

import com.thoughtworks.archguard.architecture.domain.model.ArchSystem

data class ArchSystemCreateResponse(val id: String, val name: String?) {
    companion object {
        fun from(archSystem: ArchSystem): ArchSystemCreateResponse {
            return ArchSystemCreateResponse(archSystem.id, archSystem.name)
        }
    }
}
