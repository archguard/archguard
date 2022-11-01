package com.thoughtworks.archguard.architecture.domain.model

class Architecture {
    var style: ArchStyle? = null
    var components: List<ArchComponent> = ArrayList()
    var connections: List<ArchComponentConnection> = ArrayList()
    var liner: ArchLinter? = null

    enum class ArchStyle {
        LAYERED
    }
}
