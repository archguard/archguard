package com.thoughtworks.archguard.architecture.domain.repository

import com.thoughtworks.archguard.architecture.domain.model.Architecture

data class ArchitecturePO(
    var archSystemId: String,
    var style: Architecture.ArchStyle?,
) {
    companion object {
        fun from(architecture: Architecture): ArchitecturePO {
            return ArchitecturePO(architecture.archSystemId, architecture.style)
        }
    }
}
