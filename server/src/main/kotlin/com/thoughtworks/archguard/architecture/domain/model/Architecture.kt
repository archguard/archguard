package com.thoughtworks.archguard.architecture.domain.model

class Architecture (val archSystemId: String) {
    var style: ArchStyle? = null
    var components: List<ArchComponent> = ArrayList()
    var connections: List<ArchComponentConnection> = ArrayList()

    enum class ArchStyle {
        LAYERED
    }
}
