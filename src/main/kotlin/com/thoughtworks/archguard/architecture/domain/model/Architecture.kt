package com.thoughtworks.archguard.architecture.domain.model

data class Architecture(
    var id: String,
    var style: ArchStyle,
    var components: ArchComponent,
    var connections: ArchComponentConnection
)

enum class ArchStyle {
    LAYERED
}