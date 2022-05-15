package com.thoughtworks.archguard.architecture.domain.repository

import com.thoughtworks.archguard.architecture.domain.model.ArchSystem

data class ArchSystemPO(
    var id: String,
    var parentId: String?,
    var name: String,
) {
    companion object {
        fun from(archSystem: ArchSystem): ArchSystemPO {
            return ArchSystemPO(archSystem.id, null, archSystem.name)
        }
    }
}
