package com.thoughtworks.archguard.architecture.domain.model

class ArchComponent(val archSystemId: String, val id: String) {
    var name: String? = null
    var type: ArchComponentType? = null
    var components: List<ArchComponent> = ArrayList()

    enum class ArchComponentType {
        MODULE
    }
}

