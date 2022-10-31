package com.thoughtworks.archguard.architecture.controller.response

import com.thoughtworks.archguard.architecture.domain.model.ArchSystem
import com.thoughtworks.archguard.architecture.domain.model.Architecture

data class ArchSystemCreateResponse(
    var id: String,
    var name: String?,
    var style: Architecture.ArchStyle?
) {
    companion object {
        fun from(archSystem: ArchSystem): ArchSystemCreateResponse {
            return ArchSystemCreateResponse(archSystem.id, archSystem.name, archSystem.architecture?.style)
        }
    }
}
