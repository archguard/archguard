package com.thoughtworks.archguard.architecture.domain.repository

import com.thoughtworks.archguard.architecture.domain.model.ArchComponent
import com.thoughtworks.archguard.architecture.domain.model.ArchComponentType

data class ArchComponentPO(
    var id: String,
    var parentId: String?,
    var archSystemId: String,
    var name: String,
    var type: ArchComponentType,
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
