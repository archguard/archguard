package com.thoughtworks.archguard.architecture.domain.model.analyze

class ArchComponent private constructor(val id: String) {
    var name: String? = null
    var type: ArchComponentType? = null

    enum class ArchComponentType {
        MODULE
    }

    companion object {
        fun build(id: String): ArchComponent {
            return ArchComponent(id)
        }
    }
}

