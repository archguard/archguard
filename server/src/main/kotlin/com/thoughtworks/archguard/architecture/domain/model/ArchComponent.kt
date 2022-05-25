package com.thoughtworks.archguard.architecture.domain.model

data class ArchComponent(
    var id: String,
    var archSystemId: String,
    var name: String,
    var type: ArchComponentType,
    var components: List<ArchComponent>
)

enum class ArchComponentType {
    MODULE
}
