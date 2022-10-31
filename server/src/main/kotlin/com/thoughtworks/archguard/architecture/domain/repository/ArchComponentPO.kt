package com.thoughtworks.archguard.architecture.domain.repository

import com.thoughtworks.archguard.architecture.domain.model.ArchComponent

data class ArchComponentPO(
    var id: String,
    var parentId: String?,
    var archSystemId: String,
    var name: String?,
    var type: ArchComponent.ArchComponentType?,
) {
    companion object {
        fun from(archComponent: ArchComponent): ArchComponentPO {
            return ArchComponentPO(
                archComponent.id,
                null,
                archComponent.archSystemId,
                archComponent.name,
                archComponent.type
            )
        }
    }
}
