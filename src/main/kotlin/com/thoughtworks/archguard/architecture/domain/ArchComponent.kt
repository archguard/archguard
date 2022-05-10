package com.thoughtworks.archguard.architecture.domain

data class ArchComponent(
    var id: String,
    var name: String,
    var type: ArchComponentType,
    var components: List<ArchComponent>
)

enum class ArchComponentType {
    MODULE
}
