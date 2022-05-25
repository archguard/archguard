package com.thoughtworks.archguard.architecture.controller.response

import com.thoughtworks.archguard.architecture.domain.model.ArchStyle
import com.thoughtworks.archguard.architecture.domain.model.ArchSystem

data class ArchSystemCreateResponse(
    var id: String,
    var name: String,
    var style: ArchStyle?
) {
    companion object {
        fun from(archSystem: ArchSystem): ArchSystemCreateResponse {
            return ArchSystemCreateResponse(archSystem.id, archSystem.name, archSystem.architecture?.style)
        }
    }
}
