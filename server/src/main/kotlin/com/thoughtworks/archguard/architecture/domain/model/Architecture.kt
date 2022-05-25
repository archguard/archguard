package com.thoughtworks.archguard.architecture.domain.model

data class Architecture(
    var archSystemId: String,
    var style: ArchStyle,
    var components: List<ArchComponent>,
    var connections: List<ArchComponentConnection>
)

enum class ArchStyle {
    LAYERED
}
