package com.thoughtworks.archguard.architecture.domain.model

class ArchComponent private constructor(val archSystemId: String, val id: String) {
    var name: String? = null
    var type: ArchComponentType? = null

    enum class ArchComponentType {
        MODULE
    }

    companion object {
        fun build(archSystemId: String, id: String): ArchComponent {
            return ArchComponent(archSystemId, id)
        }
    }
}

